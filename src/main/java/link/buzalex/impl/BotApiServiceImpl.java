package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.models.BotMessageReply;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Function;

@Component
public class BotApiServiceImpl implements BotApiService {
    private Function<? super BotApiMethod, Object> executor;

    @Override
    public Message sendToUser(BotMessageReply message, Long id) {
        return execute(message, id);
    }

    @Override
    public Message sendToUser(String message, Long id, String parseMode) {

        return  (Message) executor.apply(SendMessage.builder()
                .text(message)
                .chatId(id)
                .parseMode(parseMode)
                .build());
    }

    @Override
    public Message editMessage(Long chatId, Integer messageId, String newText, String parseMode) {
        return (Message) executor.apply(EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(newText)
                .parseMode(parseMode)
                .build());
    }

    @Override
    public void setExecutor(Function<? super BotApiMethod, Object> consumer) {
        this.executor = consumer;
    }

    public Message execute(BotMessageReply message, Long id) {

        return (Message) executor.apply(SendMessage.builder()
                .text(message.text())
                .replyMarkup(message.keyboard())
                .chatId(id)
                .build());
    }

    @Override
    public void clear(int messageId, Long chatId) {
        executor.apply(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }
}
