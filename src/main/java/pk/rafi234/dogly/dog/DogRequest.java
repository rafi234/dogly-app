package pk.rafi234.dogly.dog;

import java.util.Date;

public record DogRequest(String id, String name, Date dogsBirth, String breed) {
}
