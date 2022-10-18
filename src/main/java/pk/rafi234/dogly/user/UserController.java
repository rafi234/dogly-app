package pk.rafi234.dogly.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.user.dto.PasswordChangeRequest;
import pk.rafi234.dogly.user.dto.PasswordChangeResponse;
import pk.rafi234.dogly.user.dto.UserRequest;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userDetailsService.addUser(userRequest));
    }

    @GetMapping("/api/user")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userDetailsService.getAll());
    }

    @GetMapping("/api/user/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userDetailsService.getUser(email));
    }

    @DeleteMapping("/api/user/{email}")
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(userDetailsService.deleteUser(email));
    }

    @PutMapping("/api/user")
    public ResponseEntity<PasswordChangeResponse> updatePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        return ResponseEntity.ok(userDetailsService.updatePassword(passwordChangeRequest.getNewPassword()));
    }
}
