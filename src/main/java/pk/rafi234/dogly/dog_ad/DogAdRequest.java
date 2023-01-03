package pk.rafi234.dogly.dog_ad;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.user.dto.UserRequest;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public final class DogAdRequest {

    private String id;
    private Set<String> dogIds;
    private String description;
    private LocalDateTime date;
    private UserRequest user;
    private double price;
    private AdState adState;
}
