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

    @PutMapping("/confirm")
    public void confirmDogAd(@RequestBody DogAdRequest dogAdRequest) {
        dogAdService.confirmDogAd(dogAdRequest);
    }

    @PutMapping("/confirm/forbid")
    public void deniedDogAd(@RequestBody DogAdRequest dogAdRequest) {
        dogAdService.forbidConfirmation(dogAdRequest);
    }

    @PutMapping("/confirm/allow")
    public void allowDogAd(@RequestBody DogAdRequest dogAdRequest) {
        dogAdService.allowConfirmation(dogAdRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteDogAd(@PathVariable String id) {
        System.out.println(id);
        dogAdService.deleteDogAd(id);
    }

    @GetMapping("/user")
    public ResponseEntity<List<DogAdResponse>> getUserDogAds() {
        return ResponseEntity.ok(dogAdService.getDogAds());
    }
}
