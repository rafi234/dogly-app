package pk.rafi234.dogly.meetings.scrapler;

import org.springframework.data.repository.CrudRepository;
import pk.rafi234.dogly.dog.Dog;

import java.util.List;

public interface DogParkRepository extends CrudRepository<DogPark, Integer> {

    List<DogPark> findAll();
    List<DogPark> findAllByCity(String city);
    List<DogPark> findAllByVoivodeship(String voivodeship);
}
