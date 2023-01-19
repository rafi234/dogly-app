package pk.rafi234.dogly.security.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pk.rafi234.dogly.dog_ad.exception.ItselfWalkConfirmationException;
import pk.rafi234.dogly.meetings.exception.WrongRequestParamsException;
import pk.rafi234.dogly.security.exception.OneAdminIsNeededException;
import pk.rafi234.dogly.security.exception.OnePermissionIsNeededException;
import pk.rafi234.dogly.security.exception.OnlyOneOwnerException;
import pk.rafi234.dogly.user.user_exception.DeleteOwnerException;
import pk.rafi234.dogly.user.user_exception.UploadingFilesException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(OneAdminIsNeededException.class)
    public ResponseEntity<ErrorResponse> handleOneAdminIsNeededException(OneAdminIsNeededException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OnePermissionIsNeededException.class)
    public ResponseEntity<ErrorResponse> handleOnePermissionIsNeededException(OnePermissionIsNeededException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OnlyOneOwnerException.class)
    public ResponseEntity<ErrorResponse> handleOnlyOneOwnerException(OnlyOneOwnerException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DeleteOwnerException.class)
    public ResponseEntity<ErrorResponse> handleDeleteOwnerException(DeleteOwnerException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UploadingFilesException.class)
    public ResponseEntity<ErrorResponse> handleUploadingFilesException(UploadingFilesException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongRequestParamsException.class)
    public ResponseEntity<ErrorResponse> handleWrongRequestParamsException(WrongRequestParamsException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItselfWalkConfirmationException.class)
    public ResponseEntity<ErrorResponse> handleItselfWalkConfirmationException(ItselfWalkConfirmationException ex) {
        return  new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
