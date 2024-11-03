package link.buzalex.api;

import link.buzalex.models.message.BotMessage;

public interface UserContextInitializer<T extends UserContext> {
    T initUser(BotMessage botMessage);
}
