package pk.rafi234.dogly.dog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DogController {

    private final IDogService dogService;

    @PostMapping("/api/dog")
    public ResponseEntity<DogResponse> createDog(@RequestBody DogRequest dogRequest) {
        return ResponseEntity.ok(dogService.addDog(dogRequest));
    }
}
