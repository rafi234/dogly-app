package pk.rafi234.dogly.user.user_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserAlreadyExist extends ResponseStatusException {
    public UserAlreadyExist(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
