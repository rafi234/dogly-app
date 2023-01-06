package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.security.annotation.IsUser;
import pk.rafi234.dogly.security.annotation.IsUserLogged;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog/walks")
public class DogAdController {

    private final DogAdService dogAdService;

    @GetMapping()
    public ResponseEntity<List<DogAdResponse>> getAllDogAds(@RequestParam String page) {
        return ResponseEntity.ok(dogAdService.getAllDogAds(page));
    }

    @PostMapping()
    @IsUser
    public ResponseEntity<DogAdResponse> createDogAd(@RequestBody DogAdRequest dogAdRequest) {
        return ResponseEntity.ok(dogAdService.addDogAd(dogAdRequest));
    }


    @PutMapping("/confirm")
    @IsUser
    public void processingDogAd(
            @RequestParam(required = false) String action,
            @RequestBody DogAdRequest dogAdRequest
    ) {
        dogAdService.processDogAd(dogAdRequest, action);
    }

    @DeleteMapping("/{id}")
    @IsUserLogged
    public void deleteDogAd(@PathVariable String id) {
        System.out.println(id);
        dogAdService.deleteDogAd(id);
    }

    @GetMapping("/user")
    @IsUserLogged
    public ResponseEntity<List<DogAdResponse>> getUserDogAds() {
        return ResponseEntity.ok(dogAdService.getDogAds());
    }
}
