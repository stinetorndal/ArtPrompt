package app.exceptions;

//Bruges ved fetching fra Rijksmuseum og fetching fx går galt første gang
public class RetryableApiException extends ApiException {
    public RetryableApiException(String msg) {
        super(500, msg);
    }
}