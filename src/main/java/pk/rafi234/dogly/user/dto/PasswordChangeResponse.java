package pk.rafi234.dogly.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeResponse {
    private String email;
    private String status;
}
