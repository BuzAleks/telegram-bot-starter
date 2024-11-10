package link.buzalex.api;

import link.buzalex.models.message.BotMessageReply;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface BotApiService {
    Integer sendToUser(BotMessageReply message, Long id);

    void sendToUser(String message, Long id, String parseMode);

    void editKeyboard(BotMessageReply message, Long id, Integer messageId);

    void setExecutor(Function<? super BotApiMethod, Object> consumer);

    void clear(int messageId, Long chatId);

}
