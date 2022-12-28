package pk.rafi234.dogly.image;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pk.rafi234.dogly.dog.Dog;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Image implements Serializable {

    @Id
    private UUID id;
    private String name;
    private String type;
    @Lob
    @Column(length = 50000000)
    private byte[] picByte;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Dog dog;

    public Image(String name, String type, byte[] picByte) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
