package link.buzalex.api;

import link.buzalex.models.BotMessageReply;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.function.Function;

public interface BotApiService {
    SendMessage sendToUser(BotMessageReply message, Long id);

    SendMessage sendToUser(String message, Long id, String parseMode);

    EditMessageText editMessage(Long chatId, Integer messageId, String newText, String parseMode);

    void setExecutor(Function<? super BotApiMethod, Object> consumer);

    void clear(int messageId, Long chatId);
}
