package pk.rafi234.dogly.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.security.annotation.IsAdminOrOwner;
import pk.rafi234.dogly.security.annotation.IsUserLogged;
import pk.rafi234.dogly.user.dto.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userDetailsService.addUser(userRequest));
    }

    @PutMapping("/api/logout")
    @IsUserLogged
    public void logout() {
        userDetailsService.setStateOfUser(false);
    }

    @PostMapping("/api/authenticate")
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.createJwtToken(jwtRequest));
    }

    @GetMapping("/api/user")
    @IsAdminOrOwner
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userDetailsService.getAll());
    }

    @GetMapping("/api/user/{email}")
    @IsUserLogged
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userDetailsService.getUser(email));
    }

    @GetMapping("/api/user/logged")
    @IsUserLogged
    public ResponseEntity<UserResponse> getLoggedUser() {
        return ResponseEntity.ok(userDetailsService.getLoggedUser());
    }

    @DeleteMapping("/api/user/{email}")
    @IsUserLogged
    public void deleteUser(@PathVariable String email) {
        userDetailsService.deleteUser(email);
    }

    @PutMapping("/api/user/update/password")
    @IsAdminOrOwner
    public ResponseEntity<PasswordChangeResponse> updatePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        return ResponseEntity.ok(
                userDetailsService.updatePassword(passwordChangeRequest.getNewPassword(), passwordChangeRequest.getId())
        );
    }

    @PutMapping("/api/user/update")
    @IsUserLogged
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userDetailsService.updateUser(userRequest));
    }
}
