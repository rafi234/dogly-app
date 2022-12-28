package pk.rafi234.dogly.dog_ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DogAdResponse> createDogAd(
            @RequestPart("dogAd") DogAdRequest dogAdRequest,
            @RequestPart("imageFile") MultipartFile[] file
            ) {
        return ResponseEntity.ok(dogAdService.addDogAd(dogAdRequest, file));
    }
}
