package link.buzalex.api;

import link.buzalex.models.message.BotMessage;

public interface BotMenuUserManager {
    void handleMessage(BotMessage message);
}
