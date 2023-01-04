package pk.rafi234.dogly.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public final class UserRequest {

    private String id;
    @NotBlank
    private String name;
    private String surname;
    @NotBlank
    @Pattern(regexp = "\\w+@\\w+\\.\\w+")
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}")
    private String password;

    private String country;
    private String voivodeship;
    private String city;
    private String street;
    private int phoneNumber;
    @JsonProperty(value = "postal_code")
    private String postalCode;
}
