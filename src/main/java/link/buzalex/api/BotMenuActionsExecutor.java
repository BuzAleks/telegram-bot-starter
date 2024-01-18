package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.menu.BaseStepActions;

public interface BotMenuActionsExecutor {
    void execute(BotMessage botMessage, UserContext userContext, BaseStepActions actions);
}
