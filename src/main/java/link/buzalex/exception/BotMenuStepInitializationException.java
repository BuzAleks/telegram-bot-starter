package link.buzalex.exception;

public class BotMenuStepInitializationException extends RuntimeException{
    public BotMenuStepInitializationException(String message) {
        super(message);
    }

    public BotMenuStepInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
