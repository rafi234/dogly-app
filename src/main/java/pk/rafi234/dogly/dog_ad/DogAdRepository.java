package pk.rafi234.dogly.dog_ad;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pk.rafi234.dogly.user.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface DogAdRepository extends CrudRepository<DogAd, UUID> {
    List<DogAd> findAll();
    List<DogAd> findAllByAdState(AdState state);
    List<DogAd> findAllByUserAndAdState(User user, AdState state);
    List<DogAd> findAllByConfirmedUserAndAdState(User user, AdState state);
}
