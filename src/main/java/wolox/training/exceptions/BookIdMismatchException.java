package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The book you attempted to modify did not have the expected id")
public class BookIdMismatchException extends Exception {

    public BookIdMismatchException(){
        super();
    }
}
