package pk.rafi234.dogly.dog_ad;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public final class DogAdRequest {

    private Set<String> dogIds;
    private String description;
    private LocalDateTime date;
    private double price;
}
