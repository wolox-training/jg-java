package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Book non owned")
public class BookNonOwnedException extends Exception {
    public BookNonOwnedException(){
        super();
    }

}
