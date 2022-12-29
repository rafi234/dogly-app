package pk.rafi234.dogly.dog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.dog_ad.DogAd;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Dog implements Serializable {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @NotBlank
    private String name;
    private String breed;
    @Temporal(TemporalType.DATE)
    private Date dogsBirth;

    @OneToMany( mappedBy = "dog", cascade = {CascadeType.ALL})
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "dog")
    private List<DogAd> dogAds = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dog dog) {
            return dog.getId().equals(this.id);
        }
        return false;
    }
}
