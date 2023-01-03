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

    public List<DogAdResponse> getAllDogAds() {
        List<DogAd> dogAds = dogAdRepository.findAllByConfirmedUserIsNull();
        return mapDogAdsListToDTO(dogAds);
    }

    public void confirmDogAd(DogAdRequest dogAdRequest) {
        LocalDateTime confirmedAt = LocalDateTime.now();
        User user = authenticationFacade.getAuthentication();
        if (user.getId().toString().equals(dogAdRequest.getUser().getId()))
            throw new RuntimeException("User can not confirm the walk created by itself");
        setConfirmation(dogAdRequest, confirmedAt, user, WAITING_FOR_CONFIRMATION);
    }

    public void forbidConfirmation(DogAdRequest dogAdRequest) {
        setConfirmation(dogAdRequest, null, null, DENIED);
    }

    public void allowConfirmation(DogAdRequest dogAdRequest) {
        setConfirmation(dogAdRequest);
    }

    public List<DogAdResponse> getDogAds() {
        User user = authenticationFacade.getAuthentication();
        List<DogAd> dogAds = new ArrayList<>();
        List<DogAd> dogAdsWaiting = dogAdRepository
                .findAllByUserAndAdState(user, WAITING_FOR_CONFIRMATION);
        List<DogAd> dogAdsAllowed = dogAdRepository.findAllByUserAndAdState(user, ALLOWED);
        List<DogAd> dogAdsForbid = dogAdRepository.findAllByUserAndAdState(user, DENIED);
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
    public void deleteExpiredDogAds() {
        List<DogAd> dogAds = dogAdRepository.findAll();
        dogAds.forEach(this::deleteExpiredDogAd);
    }

    private void setConfirmation(
            DogAdRequest dogAdRequest,
            LocalDateTime confirmedAt,
            User user,
            AdState state
    ) {
        UUID id = UUID.fromString(dogAdRequest.getId());
        DogAd dogAd = dogAdRepository.findById(id).orElseThrow();
        dogAd.setConfirmedAt(confirmedAt);
        dogAd.setConfirmedUser(user);
        dogAd.setAdState(state);
        dogAdRepository.save(dogAd);
    }

    private void setConfirmation(
            DogAdRequest dogAdRequest
    ) {
        UUID id = UUID.fromString(dogAdRequest.getId());
        DogAd dogAd = dogAdRepository.findById(id).orElseThrow();
        dogAd.setConfirmedAt(null);
        dogAd.setAdState(AdState.ALLOWED);
        dogAdRepository.save(dogAd);
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

    private List<DogAdResponse> mapDogAdsListToDTO(List<DogAd> dogAds) {
        return dogAds.stream()
                .map(DogAdResponse::new)
                .collect(Collectors.toList());
    }
}
