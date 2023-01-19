package pk.rafi234.dogly.dog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.image.ImageService;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.security.role.Group;
import pk.rafi234.dogly.user.User;
import pk.rafi234.dogly.user.user_exception.UploadingFilesException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static pk.rafi234.dogly.security.role.Role.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class DogServiceImpl implements IDogService {

    private final DogRepository dogRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final ImageService imageService;

    @Override
    public DogResponse addDog(DogRequest dogRequest, MultipartFile[] files) {

        Dog dog = new Dog();
        dog.setId(UUID.randomUUID());
        dog.setName(dogRequest.name());
        dog.setBreed(dogRequest.breed());
        dog.setDogsBirth(dogRequest.dogsBirth());
        dog.setOwner(authenticationFacade.getAuthentication());

        Dog savedDog = dogRepository.save(dog);
        prepareImages(savedDog, files);
        return new DogResponse(savedDog);
    }

    @Override
    public DogResponse editDog(DogRequest dogRequest, MultipartFile[] files) {
        Dog dog = dogRepository.findById(UUID.fromString(dogRequest.id())).orElseThrow();

        if (!dogRequest.dogsBirth().equals(dog.getDogsBirth()))
            dog.setDogsBirth(dogRequest.dogsBirth());

        if (!dogRequest.name().equals(dog.getName()))
            dog.setName(dogRequest.name());

        if (!dogRequest.breed().equals(dog.getBreed()))
            dog.setBreed(dogRequest.breed());

        imageService.deleteAllDogImages(dog);
        dog.setImages(new HashSet<>());
        Dog savedDog = dogRepository.save(dog);
        prepareImages(savedDog, files);

        return new DogResponse(savedDog);
    }

    @Override
    public List<DogResponse> getAllDog() {
        List<Dog> dogs = (List<Dog>) dogRepository.findAll();
        return dogListToDogResponseList(dogs);
    }

    @Override
    public List<DogResponse> getLoggedUserDog() {
        User owner = authenticationFacade.getAuthentication();
        List<Dog> dogs;
        Group groupAdmin = new Group(ADMIN);
        if (owner.getRoles().stream().anyMatch(role -> role.equals(groupAdmin)))
            dogs = (List<Dog>) dogRepository.findAll();
        else
            dogs = dogRepository.findByOwner(owner);
        return dogListToDogResponseList(dogs);
    }

    @Override
    public void deleteDogById(String id) {
        UUID uuid = UUID.fromString(id);
        dogRepository.deleteById(uuid);
    }

    private void prepareImages(Dog savedDog, MultipartFile[] files) {
        try {
            Set<Image> images = imageService.uploadImage(files);

            images.forEach(i -> {
                    i.setDog(savedDog);
                    imageService.saveImage(i);
            });
        } catch (IOException e) {
            throw new UploadingFilesException("Problems with uploading files!");
        }

    }

    private List<DogResponse> dogListToDogResponseList(List<Dog> dogs) {
        return dogs.stream()
                .map(DogResponse::new)
                .collect(Collectors.toList());
    }
}
