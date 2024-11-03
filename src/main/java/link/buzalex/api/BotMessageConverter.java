package link.buzalex.api;

import link.buzalex.models.message.BotMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotMessageConverter {
    BotMessage convert(Update update);
}
