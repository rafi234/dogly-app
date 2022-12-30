package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog/walks")
public class DogAdController {

    private final DogAdService dogAdService;

    @GetMapping()
    public ResponseEntity<List<DogAdResponse>> getAllDogAds() {
        return ResponseEntity.ok(dogAdService.getAllDogAds());
    }

    @PostMapping()
    public ResponseEntity<DogAdResponse> createDogAd(@RequestBody DogAdRequest dogAdRequest) {
        return ResponseEntity.ok(dogAdService.addDogAd(dogAdRequest));
    }
}
