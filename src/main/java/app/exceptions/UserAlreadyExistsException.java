package app.exceptions;

public class UserAlreadyExistsException extends ApiException{

    public UserAlreadyExistsException(String message) {
        super(400, message);
    }
}
