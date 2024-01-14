package link.buzalex.exception;

public class BotApiServiceException extends RuntimeException{
    public BotApiServiceException(String message) {
        super(message);
    }

    public BotApiServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
