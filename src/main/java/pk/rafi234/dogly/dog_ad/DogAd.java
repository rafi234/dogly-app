package pk.rafi234.dogly.dog_ad;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DogAd implements Serializable {

    @Id
    private UUID id;

    @ManyToMany
    @JoinTable(
            name = "dog_ad_dogs",
            joinColumns = @JoinColumn(name = "dog_ad_id"),
            inverseJoinColumns = @JoinColumn(name = "dog_id")
    )
    private Set<Dog> dogs = new HashSet<>();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    private String description;

    private double price;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
