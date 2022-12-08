package pk.rafi234.dogly.meetings.scrapler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.dog.Dog;

import javax.transaction.Transactional;
import java.util.List;

import static pk.rafi234.dogly.meetings.scrapler.WepScrapper.getAllDogParks;

@Service
@RequiredArgsConstructor
public class DogParkService {

    private final DogParkRepository dogParkRepository;
    private final long timeToUpdate = 18000000L; // 5h
    private long lastUpdatedRequest = 0L;

    @Async
    @Transactional
    public void addParks() {
        final List<DogPark> dogParks = getAllDogParks();
        checkIfUpdateIsNeeded();
        dogParkRepository.saveAll(dogParks);
    }

    public List<DogPark> getAllParks() {
        checkIfUpdateIsNeeded();
        return dogParkRepository.findAll();
    }

    public List<DogPark> getParksByCity(String city) {
        checkIfUpdateIsNeeded();
        return dogParkRepository.findAllByCity(city);
    }

    public List<DogPark> getAllParksByVoivodeship(String voivodeship) {
        checkIfUpdateIsNeeded();
        return dogParkRepository.findAllByVoivodeship(voivodeship);
    }

    public void updateDogParks() {
        final List<DogPark> dogParksLoaded = getAllDogParks();
        final List<DogPark> dogParks = dogParkRepository.findAll();
        for (DogPark dogPark : dogParksLoaded) {
            if (!isDogParkInDatabase(dogParks, dogPark)) {
                dogParkRepository.save(dogPark);
            }
        }
    }

    private boolean isDogParkInDatabase(List<DogPark> dogParks, DogPark dogPark) {
        return dogParks.stream().anyMatch(dp -> dp.getLocation().equals(dogPark.getLocation()));
    }

    private void checkIfUpdateIsNeeded() {
        final long currentTimeInMillis = System.currentTimeMillis();
        if (lastUpdatedRequest == 0L || currentTimeInMillis - lastUpdatedRequest >= timeToUpdate) {
            updateDogParks();
            lastUpdatedRequest = currentTimeInMillis;
        }
    }
}

