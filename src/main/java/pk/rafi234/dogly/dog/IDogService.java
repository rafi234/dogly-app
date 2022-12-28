package pk.rafi234.dogly.dog;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDogService {
    DogResponse addDog(DogRequest dogRequest, MultipartFile[] files);

    List<DogResponse> getLoggedUserDog();
}
