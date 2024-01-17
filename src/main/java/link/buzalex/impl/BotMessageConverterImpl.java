package link.buzalex.impl;

import link.buzalex.api.BotMessageConverter;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotMessageConverterImpl implements BotMessageConverter {
    @Override
    public BotMessage convert(Update update) {
        // TODO: 16.01.2024 define more converters for message

        if (update.hasMessage() && update.getMessage().hasText()) {
            final Message message = update.getMessage();
            return BotMessageBuilder.builder()
                    .userId(message.getFrom().getId())
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(message.getText())
                    .build();
        }
        return null;
    }
}
