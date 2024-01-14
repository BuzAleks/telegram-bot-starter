package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface UserContextInitializer<T extends UserContext> {
    T initUser(BotMessage botMessage);
}
