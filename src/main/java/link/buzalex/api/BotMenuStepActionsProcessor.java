package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface BotMenuStepActionsProcessor {
    void processStep(BotMessage botMessage, UserContext userContext);
}
