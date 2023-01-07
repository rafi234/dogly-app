package pk.rafi234.dogly.dog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.image.ImageResponse;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public final class DogResponse {
    private UUID id;
    private String name;
    private Date dogsBirth;
    private String breed;
    private String ownerName;
    private String ownerSurname;
    private String ownerEmail;
    private Set<ImageResponse> images;

    public DogResponse(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.dogsBirth = dog.getDogsBirth();
        this.breed = dog.getBreed();
        this.ownerName = dog.getOwner().getName();
        this.ownerSurname = dog.getOwner().getSurname();
        this.ownerEmail = dog.getOwner().getEmail();
        this.images = prepareImages(dog.getImages());
    }

    private Set<ImageResponse> prepareImages(Set<Image> images) {
        return images.stream()
                .map(ImageResponse::new)
                .collect(Collectors.toSet());
    }
}
