package pk.rafi234.dogly.dog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public final class DogResponse {
    private UUID id;
    private String name;
    private String birth;
    private String breed;

    public DogResponse(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.birth = dog.getDogsBirth().toString();
        this.breed = dog.getBreed();
    }
}
