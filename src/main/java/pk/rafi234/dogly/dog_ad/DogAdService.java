package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.dog.DogRepository;
import pk.rafi234.dogly.image.Image;
import pk.rafi234.dogly.image.ImageService;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogAdService {

    private final DogAdRepository dogAdRepository;
    private final DogRepository dogRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final ImageService imageService;


    @Transactional
    public DogAdResponse addDogAd(DogAdRequest dogAdRequest, MultipartFile[] file) {

        DogAd dogAd = new DogAd();
        UUID id = UUID.fromString(dogAdRequest.getDogId());
        Dog dog = findDogOrThrow(id);
        dogAd.setDog(dog);
        dogAd.setId(UUID.randomUUID());
        dogAd.setDate(dogAdRequest.getDate());
        dogAd.setDescription(dogAd.getDescription());
        dogAd.setPrice(dogAd.getPrice());
        dogAd.setUser(authenticationFacade.getAuthentication());
//      Set<Image> images = imageService.uploadImage(file);
//      images.forEach(dogAd::setImage);
        return new DogAdResponse(dogAdRepository.save(dogAd));

    }

    public List<DogAdResponse> getAllDogAds() {
        return dogAdRepository.findAll().stream()
                .map(DogAdResponse::new)
                .collect(Collectors.toList());
    }

    private Dog findDogOrThrow(UUID id) {
        return dogRepository.findById(id).orElseThrow();
    }
}
