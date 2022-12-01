package pk.rafi234.dogly.meetings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Meeting {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    private int interested = 0;
    private int going = 0;

}
