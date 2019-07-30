package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Book already owned")
public class BookAlreadyOwnedException extends Exception{
    public BookAlreadyOwnedException(){
        super();
    }
}
