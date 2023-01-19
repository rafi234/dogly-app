package pk.rafi234.dogly.image;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.user.User;

import java.util.UUID;

@Repository
public interface ImageRepository extends CrudRepository<Image, UUID> {
    void deleteByDog(Dog dog);
    void deleteByUser(User user);
}
