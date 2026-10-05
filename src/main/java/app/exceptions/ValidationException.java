package app.exceptions;

public class ValidationException extends ApiException{

    public ValidationException(String message){
        super (400, message);
    }
}
