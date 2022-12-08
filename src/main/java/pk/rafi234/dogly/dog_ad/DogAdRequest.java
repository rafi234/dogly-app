package pk.rafi234.dogly.dog_ad;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public final class DogAdRequest {

    private String dogId;
    private String description;
    private LocalDateTime date;
}
