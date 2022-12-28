package pk.rafi234.dogly.dog_ad;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DogAd implements Serializable {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    private String description;

    private double price;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;


}
