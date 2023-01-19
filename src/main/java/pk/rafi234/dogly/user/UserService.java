package pk.rafi234.dogly.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.image.ImageService;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.security.role.Group;
import pk.rafi234.dogly.security.role.GroupRepository;
import pk.rafi234.dogly.security.role.Role;
import pk.rafi234.dogly.security.util.JwtUtil;
import pk.rafi234.dogly.user.address.Address;
import pk.rafi234.dogly.user.address.AddressRepository;
import pk.rafi234.dogly.user.dto.*;
import pk.rafi234.dogly.user.user_exception.DeleteOwnerException;
import pk.rafi234.dogly.user.user_exception.UploadingFilesException;
import pk.rafi234.dogly.user.user_exception.UserAlreadyExist;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements CustomUserDetailsService {

    private final AddressRepository addressRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAuthenticationFacade authenticationFacade;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ImageService imageService;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse addUser(UserRequest userRequest, MultipartFile[] multipartFiles) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExist("User with " + userRequest.getEmail() + " already exist!");
        }
        Address address = createAddress(userRequest);
        User user = new User(
                UUID.randomUUID(),
                userRequest.getName(),
                userRequest.getSurname(),
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword()),
                userRequest.getPhoneNumber()
        );
        addDefaultRole(user);
        user.setAddress(address);
        addressRepository.save(address);
        User savedUser = userRepository.save(user);
        prepareImages(savedUser, multipartFiles);
        return new UserResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUser(String email) {
        User user = getUserByEmailOrThrow(email);
        return new UserResponse(user);
    }

    @Override
    public void deleteUser(String email) {
        User user = getUserByEmailOrThrow(email);
        Group owner = groupRepository.findByRole(Role.OWNER);
        if (user.getRoles().contains(owner)) {
            throw new DeleteOwnerException("You cannot delete owner");
        }
        userRepository.delete(user);
    }

    @Override
    public PasswordChangeResponse updatePassword(String newPassword, String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found!"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new PasswordChangeResponse(user.getEmail(), "Password changed successfully!");
    }

    @Override
    public void setStateOfUser(boolean state) {
        User user = authenticationFacade.getAuthentication();
        user.setActive(state);
        userRepository.save(user);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, MultipartFile[] multipartFiles) {
        User user = userRepository.findById(UUID.fromString(userRequest.getId())).orElseThrow();
        User userToCheck = userRepository.findByEmailIgnoreCase(user.getEmail()).orElse(new User());
        if (userRequest.getEmail().equals(user.getEmail()) || userToCheck.getId() == null) {
            updateUser(user, userRequest);
            imageService.deleteAllUserImages(user);
            user.setImages(new HashSet<>());
            User savedUser = userRepository.save(user);
            if (multipartFiles != null) {
                prepareImages(savedUser, multipartFiles);
            }
            return new UserResponse(savedUser);
        }
        throw new UserAlreadyExist("User with email: " + userRequest.getEmail() + " already exist!");
    }

    @Override
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getEmail(), jwtRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmailIgnoreCase(jwtRequest.getEmail()).get();
        return new JwtResponse(new UserResponse(user), token);
    }

    @Override
    public UserResponse getLoggedUser() {
        User loggedUser = authenticationFacade.getAuthentication();
        return new UserResponse(loggedUser);
    }

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email" + email + " not found!"));
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private Address createAddress(UserRequest userRequest) {
        return Address.builder()
                .id(UUID.randomUUID())
                .street(userRequest.getStreet())
                .country(userRequest.getCountry())
                .voivodeship(userRequest.getVoivodeship())
                .city(userRequest.getCity())
                .postalCode(userRequest.getPostalCode())
                .build();
    }

    private void addDefaultRole(User user) {
        Group group = groupRepository.findByRole(Role.USER);
        if (userRepository.count() == 0) {
            group = groupRepository.findByRole(Role.ADMIN);
        }
        user.getRoles().add(group);
    }

    private void updateUser(User user, UserRequest userRequest) {

        String newEmail = userRequest.getEmail();
        if (newEmail != null) {
            user.setEmail(userRequest.getEmail());
        }

        String newName = userRequest.getName();
        if (newName != null) {
            user.setName(newName);
        }

        String newSurname = userRequest.getSurname();
        if (newSurname != null) {
            user.setSurname(newSurname);
        }

        String newStreet = userRequest.getStreet();
        if (newStreet != null) {
            user.getAddress().setStreet(newStreet);
        }

        String newCountry = userRequest.getCountry();
        if (newCountry != null) {
            user.getAddress().setCountry(newCountry);
        }

        String newVoivodeship = userRequest.getVoivodeship();
        if (newVoivodeship != null) {
            user.getAddress().setVoivodeship(newVoivodeship);
        }

        String newPostalCode = userRequest.getPostalCode();
        if (newPostalCode != null) {
            user.getAddress().setPostalCode(newPostalCode);
        }

        String newCity = userRequest.getCity();
        if (newCity != null) {
            user.getAddress().setCity(newCity);
        }

        int newPhoneNumber = userRequest.getPhoneNumber();
        if (newCity != null) {
            user.setPhoneNumber(newPhoneNumber);
        }
    }

    private void prepareImages(User savedUser, MultipartFile[] files) {
        try {
            Set<Image> images = imageService.uploadImage(files);

            images.forEach(i -> {
                i.setUser(savedUser);
                imageService.saveImage(i);
            });
        } catch (IOException e) {
            throw new UploadingFilesException("Problems with uploading files!");
        }

    }
}
