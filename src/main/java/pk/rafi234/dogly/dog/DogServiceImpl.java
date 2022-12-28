package pk.rafi234.dogly.dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.image.ImageService;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.user.User;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DogServiceImpl implements IDogService {

    private final DogRepository dogRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final ImageService imageService;

    @Autowired
    public DogServiceImpl(DogRepository dogRepository, IAuthenticationFacade authenticationFacade, ImageService imageService) {
        this.dogRepository = dogRepository;
        this.authenticationFacade = authenticationFacade;
        this.imageService = imageService;
    }

    @Override
    public DogResponse addDog(DogRequest dogRequest, MultipartFile[] files) {
        try {
            Dog dog = new Dog();
            dog.setId(UUID.randomUUID());
            dog.setName(dogRequest.name());
            dog.setBreed(dogRequest.breed());
            dog.setDogsBirth(dogRequest.dogsBirth());
            dog.setOwner(authenticationFacade.getAuthentication());
            Set<Image> images = imageService.uploadImage(files);
            Dog savedDog = dogRepository.save(dog);
            images.forEach(i -> {
                i.setDog(savedDog);
                imageService.saveImage(i);
            });
            return new DogResponse(savedDog);
        } catch (IOException e) {
            throw new RuntimeException("Problems with uploading files!");
        }
    }

    @Override
    public List<DogResponse> getLoggedUserDog() {
        User owner = authenticationFacade.getAuthentication();
        List<Dog> dogs = dogRepository.findByOwner(owner);
        return dogs.stream()
                .map(DogResponse::new)
                .collect(Collectors.toList());
    }
}
