package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.models.message.BotMessageReply;
import link.buzalex.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class BotApiServiceImpl implements BotApiService {
    private Function<? super BotApiMethod, Object> executor;

    @Override
    public Integer sendToUser(BotMessageReply message, Long id) {

        Message response = (Message) executor.apply(SendMessage.builder()
                .text(message.text())
                .replyMarkup(BotUtils.convertToInlineKeyboard(message.keyboard()))
                .chatId(id)
                .build());
        return response.getMessageId();
    }

    @Override
    public void sendToUser(String message, Long id, String parseMode) {

        executor.apply(SendMessage.builder()
                .text(message)
                .chatId(id)
                .parseMode(parseMode)
                .build());
    }

    @Override
    public void editKeyboard(BotMessageReply message, Long id, Integer messageId) {
        executor.apply(EditMessageReplyMarkup.builder()
                .replyMarkup(BotUtils.convertToInlineKeyboard(message.keyboard()))
                .messageId(messageId)
                .chatId(id)
                .build());
    }

    @Override
    public void setExecutor(Function<? super BotApiMethod, Object> executor) {
        this.executor = executor;
    }


    public void execute(BotMessageReply message, Long id) {

        executor.apply(SendMessage.builder()
                .text(message.text())
                .replyMarkup(BotUtils.convertToInlineKeyboard(message.keyboard()))
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
