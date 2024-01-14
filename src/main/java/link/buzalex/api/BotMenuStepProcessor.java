package link.buzalex.api;

import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface BotMenuStepProcessor {
    void beforeStepExecution(BotMessage botMessage, UserContext userContext);

    void afterStepExecution(BotMessage botMessage, UserContext userContext, BotActions actions);
}
