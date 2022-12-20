package pk.rafi234.dogly.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.user.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtResponse {
    private UserResponse user;
    private String token;
}
