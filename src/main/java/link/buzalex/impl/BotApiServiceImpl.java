package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.models.BotMessageReply;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.function.Consumer;

@Component
public class BotApiServiceImpl implements BotApiService {
    private Consumer<? super BotApiMethod> executor;

    @Override
    public void sendToUser(BotMessageReply message, Long id) {
        execute(message, id);
    }

    @Override
    public void sendToUser(String message, Long id, String parseMode) {

        executor.accept(SendMessage.builder()
                .text(message)
                .chatId(id)
                .parseMode(parseMode)
                .build());
    }

    @Override
    public void editMessage(Long chatId, Integer messageId, String newText, String parseMode) {
        executor.accept(EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(newText)
                .parseMode(parseMode)
                .build());
    }

    public void execute(BotMessageReply message, Long id) {

        executor.accept(SendMessage.builder()
                .text(message.text())
                .replyMarkup(message.keyboard())
                .chatId(id)
                .build());
    }

    public void setExecutor(Consumer<? super BotApiMethod> consumer) {
        this.executor = consumer;
    }

    @Override
    public void clear(int messageId, Long chatId) {
        executor.accept(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }
}
