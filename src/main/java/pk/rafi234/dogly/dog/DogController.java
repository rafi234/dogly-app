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
public class DogController {

    private final IDogService dogService;

    @PostMapping(value = {"/api/dog"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DogResponse> createDog(@RequestPart("dog") DogRequest dogRequest,
                                                 @RequestPart("imageFile") MultipartFile[] images
    ) {
        return ResponseEntity.ok(dogService.addDog(dogRequest, images));
    }

    @GetMapping("/api/dog/user")
    public ResponseEntity<List<DogResponse>> getLoggedUserDog() {
        return ResponseEntity.ok(dogService.getLoggedUserDog());
    }
}
