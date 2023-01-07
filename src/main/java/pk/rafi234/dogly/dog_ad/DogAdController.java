package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.security.annotation.IsUser;
import pk.rafi234.dogly.security.annotation.IsUserLogged;

import javax.validation.Valid;
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
    public ResponseEntity<DogAdResponse> createDogAd(@RequestBody @Valid DogAdRequest dogAdRequest) {
        return ResponseEntity.ok(dogAdService.addDogAd(dogAdRequest));
    }


    @PutMapping("/confirm")
    @IsUser
    public void processingDogAd(
            @RequestParam(required = false) String action,
            @RequestBody @Valid DogAdRequest dogAdRequest
    ) {
        dogAdService.processDogAd(dogAdRequest, action);
    }

    @DeleteMapping("/{id}")
    @IsUserLogged
    public void deleteDogAd(@PathVariable String id) {
        dogAdService.deleteDogAd(id);
    }

    @PutMapping()
    @IsUserLogged
    public ResponseEntity<DogAdResponse> updateDogAd(@RequestBody @Valid DogAdRequest dogAdRequest) {
        System.out.println(dogAdRequest);
        return ResponseEntity.ok(dogAdService.updateDogAd(dogAdRequest));
    }

    @GetMapping("/user")
    @IsUserLogged
    public ResponseEntity<List<DogAdResponse>> getUserDogAds() {
        return ResponseEntity.ok(dogAdService.getDogAds());
    }
}
