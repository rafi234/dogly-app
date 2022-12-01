package pk.rafi234.dogly.user;

import pk.rafi234.dogly.user.dto.PasswordChangeRequest;
import pk.rafi234.dogly.user.dto.PasswordChangeResponse;
import pk.rafi234.dogly.user.dto.UserRequest;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.util.List;

public interface CustomUserDetailsService {
    UserResponse addUser(UserRequest userRequest);

    List<UserResponse> getAll();

    UserResponse getUser(String email);

    void deleteUser(String email);

    PasswordChangeResponse updatePassword(String newPassword);

    void setStateOfUser(User user, boolean state);

    UserResponse updateUser(UserRequest userRequest);
}
