package app.exceptions;

public class RetryableApiException extends ApiException {
    public RetryableApiException(String msg) {
        super(500, msg);
    }
}