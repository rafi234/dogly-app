package pk.rafi234.dogly.meetings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.meetings.scrapler.DogPark;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Meeting implements Serializable {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String description;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    @ManyToOne
    @JoinColumn(name = "dog_park_id")
    private DogPark dogPark;

    private int interested = 0;
    private int going = 0;

}
