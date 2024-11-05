package link.buzalex.api;

import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.message.BotMessage;

public interface BotMenuActionsExecutor {
    void execute(BotMessage botMessage, UserContext userContext, BaseStepAction action);
}
