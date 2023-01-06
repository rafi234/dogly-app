package pk.rafi234.dogly.security.exceptionHandler;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
}
