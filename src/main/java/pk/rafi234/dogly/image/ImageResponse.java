package pk.rafi234.dogly.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse implements Serializable {

    private String name;
    private String type;
    private byte[] picByte;

    public ImageResponse(Image image) {
        this.name = image.getName();
        this.type = image.getType();
        this.picByte = image.getPicByte();
    }
}
