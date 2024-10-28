package link.buzalex.api;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ExceptionHandler {
    void handleException(Exception ex, Update update);
}
