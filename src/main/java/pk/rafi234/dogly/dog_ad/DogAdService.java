package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.dog.DogRepository;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pk.rafi234.dogly.dog_ad.AdState.*;

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

    public List<DogAdResponse> getAllDogAds(String page) {
        List<DogAd> dogAds = new ArrayList<>();
        if (page.equals("walk"))
            dogAds = dogAdRepository.findAllByAdState(WAITING_FOR_USER);
        else if (page.equals("user")) {
            User user = authenticationFacade.getAuthentication();
            dogAds = dogAdRepository.findAllByConfirmedUserAndAdState(user, WAITING_FOR_REALIZATION);
        }
        return mapDogAdsListToDTO(dogAds);
    }

    public void processDogAd(DogAdRequest dogAdRequest, String action) {
        UUID id = UUID.fromString(dogAdRequest.getId());
        User user = authenticationFacade.getAuthentication();
        LocalDateTime confirmedAt = LocalDateTime.now();
        System.out.println("\n\n" + action + "\n\n");
        switch (action) {
            case "confirm" -> {
                if (user.getId().toString().equals(dogAdRequest.getUser().getId()))
                    throw new RuntimeException("User can not confirm the walk created by itself");
                setConfirmation(id, confirmedAt, user, WAITING_FOR_CONFIRMATION);
            }
            case "denied"    -> setConfirmation(id, confirmedAt,  DENIED);
            case "allowed"   -> setConfirmation(id, confirmedAt, ALLOWED);
            case "forbidden" -> setConfirmation(id, confirmedAt, null, WAITING_FOR_USER);
            case "confirmed" -> setConfirmation(id, confirmedAt, WAITING_FOR_REALIZATION);
            case "complited" -> setConfirmation(id, confirmedAt, COMPLITED);
        }
    }

    public List<DogAdResponse> getDogAds() {
        User user = authenticationFacade.getAuthentication();
        List<DogAd> dogAds = new ArrayList<>();
        List<DogAd> dogAdsWaiting = dogAdRepository
                .findAllByUserAndAdState(user, WAITING_FOR_CONFIRMATION);
        List<DogAd> dogAdsAllowed = dogAdRepository.findAllByConfirmedUserAndAdState(user, ALLOWED);
        List<DogAd> dogAdsForbid = dogAdRepository.findAllByConfirmedUserAndAdState(user, DENIED);
        dogAds.addAll(dogAdsForbid);
        dogAds.addAll(dogAdsAllowed);
        dogAds.addAll(dogAdsWaiting);
        return mapDogAdsListToDTO(dogAds);
    }

    public void deleteDogAd(String id) {
        UUID uuid = UUID.fromString(id);
        dogAdRepository.deleteById(uuid);
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    public void expireUnTouchedDogAds() {
        List<DogAd> dogAds = dogAdRepository.findAll();
        dogAds.forEach(this::expireUnTouchedDogAd);
    }

    private void setConfirmation(
            UUID dogAdId,
            LocalDateTime confirmedAt,
            User user,
            AdState state
    ) {
        DogAd dogAd = dogAdRepository.findById(dogAdId).orElseThrow();
        dogAd.setConfirmedAt(confirmedAt);
        dogAd.setConfirmedUser(user);
        dogAd.setAdState(state);
        dogAdRepository.save(dogAd);
    }

    private void setConfirmation(
            UUID dogAdId,
            LocalDateTime confirmedAt,
            AdState state
    ) {
        DogAd dogAd = dogAdRepository.findById(dogAdId).orElseThrow();
        dogAd.setConfirmedAt(confirmedAt);
        dogAd.setAdState(state);
        dogAdRepository.save(dogAd);
    }

    private void expireUnTouchedDogAd(DogAd dogAd) {
        if (isDogAdExpired(dogAd)) {
            if (dogAd.getAdState().equals(WAITING_FOR_REALIZATION)) {
                setConfirmation(dogAd.getId(), LocalDateTime.now(), COMPLITED);
            } else {
                setConfirmation(dogAd.getId(), LocalDateTime.now(), EXPIRED);
            }
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

    private List<DogAdResponse> mapDogAdsListToDTO(List<DogAd> dogAds) {
        return dogAds.stream()
                .map(DogAdResponse::new)
                .collect(Collectors.toList());
    }


}
