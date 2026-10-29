package app.exceptions;

//Bruges når bruger forsøger at registrere bruger, der allede findes
public class UserAlreadyExistsException extends ApiException{

    public UserAlreadyExistsException(String message) {
        super(400, message);
    }
}
