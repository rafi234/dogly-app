package pk.rafi234.dogly.meetings.scrapler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pk.rafi234.dogly.meetings.Meeting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DogPark implements Serializable {

    @Id
    private UUID id;
    private String url;
    private String type;
    private String city;
    private String location;
    private String voivodeship;
    @Column(name = "image_url")
    private String imgUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "dogPark")
    private List<Meeting> meetings = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o instanceof DogPark) {
            return ((DogPark) o).url.equals(this.url);
        }
        return false;
    }
}
