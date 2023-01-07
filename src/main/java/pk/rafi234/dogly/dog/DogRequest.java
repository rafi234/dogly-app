package pk.rafi234.dogly.dog;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public record DogRequest(String id, @NotBlank(message = "Name can't be blank!") String name, Date dogsBirth, String breed) {
}
