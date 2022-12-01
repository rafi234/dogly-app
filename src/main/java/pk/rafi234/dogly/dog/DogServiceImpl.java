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

    @Autowired
    public DogServiceImpl(DogRepository dogRepository, IAuthenticationFacade authenticationFacade) {
        this.dogRepository = dogRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public DogResponse addDog(DogRequest dogRequest) {
        Dog dog = new Dog();
        dog.setId(UUID.randomUUID());
        dog.setName(dogRequest.name());
        dog.setBreed(dogRequest.breed());
        dog.setDogsBirth(Date.valueOf(dogRequest.dogsBirth()));
        dog.setOwner(authenticationFacade.getAuthentication());
        return new DogResponse(dogRepository.save(dog));
    }
}
