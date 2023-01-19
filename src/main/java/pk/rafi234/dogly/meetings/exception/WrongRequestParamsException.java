package pk.rafi234.dogly.meetings.exception;

public class WrongRequestParamsException extends RuntimeException {

    public WrongRequestParamsException(String mess) {
        super(mess);
    }
}
