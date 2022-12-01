package pk.rafi234.dogly.meetings.scrapler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class DogPark implements Serializable {

    @Id
    private UUID id;
    private String url;
    private String type;
    private String city;
    private String location;
    private String voivodeship;


    @Override
    public boolean equals(Object o) {
        if (o instanceof DogPark) {
            return ((DogPark) o).url.equals(this.url);
        }
        return false;
    }
}
