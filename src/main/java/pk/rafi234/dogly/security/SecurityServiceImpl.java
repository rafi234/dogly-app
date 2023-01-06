package pk.rafi234.dogly.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.security.role.Group;
import pk.rafi234.dogly.security.role.GroupRepository;
import pk.rafi234.dogly.security.role.Role;
import pk.rafi234.dogly.user.User;
import pk.rafi234.dogly.user.UserRepository;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    final private UserRepository userRepo;
    final private GroupRepository groupRepo;

    @Override
    public UserResponse grantPermission(String role, String id, String action) {
        if (role.equals("OWNER")) {
            throw new RuntimeException("It must be only one owner!");
        }
        UUID uuid = UUID.fromString(id);
        User user = userRepo.findById(uuid).orElseThrow();
        Role newUserRole = Role.valueOf(role);
        Group group = groupRepo.findByRole(newUserRole);


        if (action.equals("grant")) {
            user.getRoles().add(group);
        } else if (action.equals("delete")) {
            if (user.getRoles().size() == 1) {
                throw new RuntimeException("User must have at least 1 permission");
            }
            if (role.equals("ADMIN")) {
                int numberOfAdmin = getNumberOfAdmins();
                if (numberOfAdmin == 1) {
                    throw new RuntimeException("It must be at least ADMIN on a server!");
                }
            }
            user.getRoles().remove(group);
        }
        return new UserResponse(userRepo.save(user));
    }

    private int getNumberOfAdmins() {
        List<User> users = (List<User>) userRepo.findAll();
        return users.stream()
                .filter(u -> u.getRoles().contains(groupRepo.findByRole(Role.ADMIN))).
                toArray().length;
    }
}
