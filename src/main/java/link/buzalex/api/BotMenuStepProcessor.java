package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface BotMenuStepProcessor {
    void processStep(BotMessage botMessage, UserContext userContext);
}
