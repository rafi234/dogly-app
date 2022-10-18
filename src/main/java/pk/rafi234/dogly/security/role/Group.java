package pk.rafi234.dogly.security.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Group(Role role) {
        this.role = role;
    }
}
