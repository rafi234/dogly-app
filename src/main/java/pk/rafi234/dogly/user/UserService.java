package pk.rafi234.dogly.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.security.role.Group;
import pk.rafi234.dogly.security.role.GroupRepository;
import pk.rafi234.dogly.security.role.Role;
import pk.rafi234.dogly.security.util.JwtUtil;
import pk.rafi234.dogly.user.address.Address;
import pk.rafi234.dogly.user.address.AddressRepository;
import pk.rafi234.dogly.user.dto.*;
import pk.rafi234.dogly.user.user_exception.UserAlreadyExist;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserService implements CustomUserDetailsService {

    private final AddressRepository addressRepository;

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final IAuthenticationFacade authenticationFacade;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(AddressRepository addressRepository, GroupRepository groupRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, IAuthenticationFacade authenticationFacade,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.addressRepository = addressRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
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
        userRepository.save(user);
        return new UserResponse(user);
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
        userRepository.delete(getUserByEmailOrThrow(email));
    }

    @Override
    public PasswordChangeResponse updatePassword(String newPassword) {
        User user = authenticationFacade.getAuthentication();
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
    public UserResponse updateUser(UserRequest userRequest) {
        User user = getUserByEmailOrThrow(userRequest.getEmail());
        updateUser(user, userRequest);
        return new UserResponse(userRepository.save(user));
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
            user.setEmail(newEmail);
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
    }
}
