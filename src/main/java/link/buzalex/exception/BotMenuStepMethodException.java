package link.buzalex.exception;

public class BotMenuStepMethodException extends RuntimeException{
    public BotMenuStepMethodException(String message) {
        super(message);
    }

    public BotMenuStepMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
