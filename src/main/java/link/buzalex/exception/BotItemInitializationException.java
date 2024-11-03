package link.buzalex.exception;

public class BotItemInitializationException extends RuntimeException{
    public BotItemInitializationException(String message) {
        super(message);
    }

    public BotItemInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
