package app.exceptions;

//Kastes ved login. Må ikke fortælle om det er pw eller email, det er en sårbarhed
public class WrongEmailOrPasswordException extends ApiException {

    public WrongEmailOrPasswordException(String message) {
        super(400, message);
    }
}
