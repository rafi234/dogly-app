package pk.rafi234.dogly.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import java.sql.Date;
import java.util.UUID;

@Service
public class DogServiceImpl implements IDogService {

    private final DogRepository dogRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final UUID uuid;

    @Autowired
    public DogServiceImpl(DogRepository dogRepository, IAuthenticationFacade authenticationFacade, UUID uuid) {
        this.dogRepository = dogRepository;
        this.authenticationFacade = authenticationFacade;
        this.uuid = uuid;
    }

    @Override
    public DogResponse addDog(DogRequest dogRequest) {
        Dog dog = new Dog();
        dog.setId(uuid);
        dog.setName(dogRequest.name());
        dog.setBreed(dogRequest.breed());
        dog.setDogsBirth(Date.valueOf(dogRequest.dogsBirth()));
        dog.setOwner(authenticationFacade.getAuthentication());
        return new DogResponse(dogRepository.save(dog));
    }
}
