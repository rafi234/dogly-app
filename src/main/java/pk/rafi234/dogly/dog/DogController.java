package pk.rafi234.dogly.dog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pk.rafi234.dogly.security.annotation.IsUser;
import pk.rafi234.dogly.security.annotation.IsUserLogged;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dog")
public class DogController {

    private final IDogService dogService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @IsUser
    public ResponseEntity<DogResponse> createDog(@RequestPart("dog") DogRequest dogRequest,
                                                 @RequestPart("imageFile") MultipartFile[] images
    ) {
        return ResponseEntity.ok(dogService.addDog(dogRequest, images));
    }

    @GetMapping("/user")
    @IsUser
    public ResponseEntity<List<DogResponse>> getLoggedUserDogs() {
        return ResponseEntity.ok(dogService.getLoggedUserDog());
    }

    @PutMapping()
    @IsUserLogged
    public ResponseEntity<DogResponse> editDog(@RequestPart("dog") DogRequest dogRequest,
                                               @RequestPart("imageFiles") MultipartFile[] files) {
        return ResponseEntity.ok(dogService.editDog(dogRequest, files));
    }

    @DeleteMapping("/{id}")
    @IsUserLogged
    public void deleteDogById(@PathVariable String id) {
        dogService.deleteDogById(id);
    }
}
