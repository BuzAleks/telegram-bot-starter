package link.buzalex.api;

import link.buzalex.models.message.BotMessage;

public interface BotMenuStepProcessor {
    void processStep(BotMessage botMessage, UserContext userContext);
}
