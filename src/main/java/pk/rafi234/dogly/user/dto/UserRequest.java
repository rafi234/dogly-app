package pk.rafi234.dogly.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Password can't be blank!")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password should have at least: 8 characters, one upper and lower case letter, a number," +
                    " also should not have any spaces.")
    private String password;

    @NotNull(message = "Phone number cannot be null!")
    private int phoneNumber;

    private String country;
    private String voivodeship;
    private String city;
    private String street;
    @JsonProperty(value = "postal_code")
    private String postalCode;
}
