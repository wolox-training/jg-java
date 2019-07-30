package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The User you attempted to modify did not have the expected id")
public class UserIdMismatchException extends Exception {

    public UserIdMismatchException(){
        super();
    }
}
