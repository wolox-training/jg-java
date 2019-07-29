package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Actor Not Found")
public class BookIdMismatchException extends Exception {

    public BookIdMismatchException(){
        super();
    }
}
