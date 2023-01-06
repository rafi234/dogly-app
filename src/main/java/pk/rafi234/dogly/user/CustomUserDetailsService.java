package pk.rafi234.dogly.user;

import pk.rafi234.dogly.user.dto.*;

import java.util.List;

public interface CustomUserDetailsService {
    UserResponse addUser(UserRequest userRequest);

    List<UserResponse> getAll();

    UserResponse getUser(String email);

    void deleteUser(String email);

    PasswordChangeResponse updatePassword(String newPassword, String email);

    void setStateOfUser(boolean state);

    UserResponse updateUser(UserRequest userRequest);

    JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception;

    UserResponse getLoggedUser();
}
