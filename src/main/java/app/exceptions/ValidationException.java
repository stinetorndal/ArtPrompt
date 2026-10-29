package app.exceptions;

//Kastes når input ikke overholder regler, fx tomt felt, ugyldig email
public class ValidationException extends ApiException{

    public ValidationException(String message){
        super (400, message);
    }
}
