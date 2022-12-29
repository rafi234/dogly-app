package pk.rafi234.dogly.dog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.image.Image;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog")
public class DogController {

    private final IDogService dogService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DogResponse> createDog(@RequestPart("dog") DogRequest dogRequest,
                                                 @RequestPart("imageFile") MultipartFile[] images
    ) {
        return ResponseEntity.ok(dogService.addDog(dogRequest, images));
    }

    @GetMapping("/user")
    public ResponseEntity<List<DogResponse>> getLoggedUserDogs() {
        return ResponseEntity.ok(dogService.getLoggedUserDog());
    }

    @PutMapping()
    public ResponseEntity<DogResponse> editDog(@RequestPart("dog") DogRequest dogRequest,
                                               @RequestPart("imageFiles") MultipartFile[] files) {
        return ResponseEntity.ok(dogService.editDog(dogRequest, files));
    }

    @DeleteMapping("/{id}")
    public void deleteDogById(@PathVariable String id) {
        dogService.deleteDogById(id);
    }
}
