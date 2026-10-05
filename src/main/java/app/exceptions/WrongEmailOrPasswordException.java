package app.exceptions;

public class WrongEmailOrPasswordException extends ApiException {

    public WrongEmailOrPasswordException(String message) {
        super(400, message);
    }
}
