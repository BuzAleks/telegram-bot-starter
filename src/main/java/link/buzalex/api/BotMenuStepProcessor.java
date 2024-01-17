package link.buzalex.api;

import link.buzalex.models.BotMessage;

public interface BotMenuStepProcessor {
    void processStep(BotMessage botMessage, UserContext userContext);
}
