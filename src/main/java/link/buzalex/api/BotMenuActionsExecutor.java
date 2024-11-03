package link.buzalex.api;

import link.buzalex.models.message.BotMessage;
import link.buzalex.models.action.BaseStepAction;

import java.util.List;

public interface BotMenuActionsExecutor {
    void execute(BotMessage botMessage, UserContext userContext, List<BaseStepAction> actions);
}
