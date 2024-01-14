package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Consumer;

@Component
public class BotApiServiceImpl implements BotApiService {
    private Consumer<? super BotApiMethod> executor;

    @Override
    public void sendToUser(String message, Long id) {
        sendToUser(message, id, null);
    }

    @Override
    public void sendToUser(String message, Long id, String parseMode) {
        executor.accept(SendMessage.builder()
                .text(message)
                .chatId(id)
                .parseMode(parseMode)
                .build());
    }

    public void setExecutor(Consumer<? super BotApiMethod> consumer) {
        this.executor = consumer;
    }
}
