package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.dog.DogRepository;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DogAdService {

    private final DogAdRepository dogAdRepository;
    private final DogRepository dogRepository;
    private final IAuthenticationFacade authenticationFacade;


    public DogAdResponse addDogAd(DogAdRequest dogAdRequest) {
        DogAd dogAd = new DogAd();
        Set<Dog> dogs = createDogsSet(dogAdRequest.getDogIds());
        dogAd.setDogs(dogs);
        dogAd.setId(UUID.randomUUID());
        dogAd.setDate(dogAdRequest.getDate());
        dogAd.setAddedAt(LocalDateTime.now());
        dogAd.setDescription(dogAdRequest.getDescription());
        dogAd.setPrice(dogAdRequest.getPrice());
        dogAd.setUser(authenticationFacade.getAuthentication());
        return new DogAdResponse(dogAdRepository.save(dogAd));
    }

    public List<DogAdResponse> getAllDogAds() {
        return dogAdRepository.findAll().stream()
                .map(DogAdResponse::new)
                .collect(Collectors.toList());
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    public void deleteExpiredDogAds() {
        List<DogAd> dogAds = dogAdRepository.findAll();
        dogAds.forEach(this::deleteExpiredDogAd);

    }

    private void deleteExpiredDogAd(DogAd dogAd) {
        if (isDogAdExpired(dogAd)) {
            dogAdRepository.deleteById(dogAd.getId());
        }
    }

    private boolean isDogAdExpired(DogAd dogAd) {
        return dogAd.getDate().isBefore(LocalDateTime.now());
    }
    private Set<Dog> createDogsSet(Set<String> dogIds) {
        return dogIds.stream().map(id -> {
                    final UUID uuid = UUID.fromString(id);
                    return findDogOrThrow(uuid);
                })
                .collect(Collectors.toSet());
    }

    private Dog findDogOrThrow(UUID id) {
        return dogRepository.findById(id).orElseThrow();
    }
}
