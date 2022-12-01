package pk.rafi234.dogly.meetings.scrapler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog/park")
public class DogParkController {
    private final DogParkService dogParkService;

    @GetMapping("")
    public ResponseEntity<List<DogPark>> getAllDogParks() {
        return ResponseEntity.ok().body(dogParkService.getAllParks());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<DogPark>> getAllDogParksByCity(@PathVariable String city) {
        return ResponseEntity.ok().body(dogParkService.getParksByCity(city));
    }

    @GetMapping("/voivodeship/{voivodeship}")
    public ResponseEntity<List<DogPark>> getAllDogParksByVoivodeship(@PathVariable String voivodeship) {
        return ResponseEntity.ok().body(dogParkService.getAllParksByVoivodeship(voivodeship));
    }
}
