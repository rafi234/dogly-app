package pk.rafi234.dogly.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public final class PasswordChangeRequest {

    @NotBlank
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password should have at least: 8 characters, one upper and lower case letter, a number," +
                    " also should not have any spaces.")
    @JsonProperty(value = "new_password")
    String newPassword;
    String id;
}
