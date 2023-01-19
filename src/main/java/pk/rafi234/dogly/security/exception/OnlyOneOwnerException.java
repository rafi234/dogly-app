package pk.rafi234.dogly.security.exception;

public class OnlyOneOwnerException extends RuntimeException {

    public OnlyOneOwnerException(String mess) {
        super(mess);
    }
}
