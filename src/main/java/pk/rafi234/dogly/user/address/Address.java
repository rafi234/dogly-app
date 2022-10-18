package pk.rafi234.dogly.user.address;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Address implements Serializable {

    @Id
    private UUID id;
    private String country;
    private String voivodeship;
    private String city;
    private String street;
    @Column(name = "postal_code")
    private String postalCode;

    @Override
    public String toString() {
        return "Country:  " + country +
                "\nVoivodeship: " + voivodeship +
                "\nCity " + city + ", " + postalCode +
                "\nStreet: " + street;
    }
}
