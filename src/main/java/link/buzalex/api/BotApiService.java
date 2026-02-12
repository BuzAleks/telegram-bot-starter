package link.buzalex.api;

import link.buzalex.models.BotMessageReply;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Function;

public interface BotApiService {
    Message sendToUser(BotMessageReply message, Long id);

    Message sendToUser(String message, Long id, String parseMode);

    Message editMessage(Long chatId, Integer messageId, String newText, String parseMode);

    void setExecutor(Function<? super BotApiMethod, Object> consumer);

    void clear(int messageId, Long chatId);
}
