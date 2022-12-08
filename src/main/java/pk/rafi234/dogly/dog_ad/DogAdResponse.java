package pk.rafi234.dogly.dog_ad;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.dog.DogResponse;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public final class DogAdResponse {

    private String id;
    private double price;
    private String description;
    private UserResponse userResponse;
    private DogResponse dogResponse;
    private LocalDateTime date;

    public DogAdResponse(DogAd dogAd) {
        this.date = dogAd.getDate();
        this.id = dogAd.getId().toString();
        this.price = dogAd.getPrice();
        this.description = dogAd.getDescription();
        this.userResponse = new UserResponse(dogAd.getUser());
        this.dogResponse = new DogResponse(dogAd.getDog());
    }
}
