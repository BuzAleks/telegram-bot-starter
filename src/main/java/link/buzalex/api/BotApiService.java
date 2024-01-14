package link.buzalex.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.function.Consumer;

public interface BotApiService {

    void sendToUser(String message, Long id);
    void sendToUser(String message, Long id, String parseMode);
    void setExecutor(Consumer<? super BotApiMethod> consumer);
}
