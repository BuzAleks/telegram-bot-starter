package link.buzalex.api;

import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.message.BotMessage;

public interface BotMenuActionsExecutor {
    ActionCursor executeAndMoveCursor(BotMessage botMessage, UserContext userContext, ActionCursor cursor);
}
