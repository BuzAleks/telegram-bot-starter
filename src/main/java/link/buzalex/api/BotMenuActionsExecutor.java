package link.buzalex.api;

import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.message.BotMessage;

public interface BotMenuActionsExecutor {
    ActionStackObject executeAndMoveCursor(BotMessage botMessage, UserContext userContext, ActionStackObject cursor);
}
