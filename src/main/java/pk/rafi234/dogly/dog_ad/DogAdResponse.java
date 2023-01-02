package pk.rafi234.dogly.dog_ad;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.dog.DogResponse;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public final class DogAdResponse {

    private String id;
    private double price;
    private String description;
    private UserResponse user;
    private Set<DogResponse> dogs;
    private LocalDateTime date;
    private LocalDateTime addedAt;

    public DogAdResponse(DogAd dogAd) {
        this.date = dogAd.getDate();
        this.addedAt = dogAd.getAddedAt();
        this.id = dogAd.getId().toString();
        this.price = dogAd.getPrice();
        this.description = dogAd.getDescription();
        this.user = new UserResponse(dogAd.getUser());
        this.dogs = createDogsSet(dogAd.getDogs());
    }

    private Set<DogResponse> createDogsSet(Set<Dog> dogs) {
        return dogs.stream()
                .map(DogResponse::new)
                .collect(Collectors.toSet());
    }
}
