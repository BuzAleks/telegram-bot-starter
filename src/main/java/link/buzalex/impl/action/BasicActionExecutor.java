package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.Action;
import link.buzalex.models.message.BotMessage;

public abstract class BasicActionExecutor<T extends Action> extends ActionExecutor<T> {

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, T action) {
        execute(botMessage, userContext, action);
        return moveCursor(cursor);
    }

    abstract void execute(BotMessage botMessage, UserContext userContext, T action);
}
