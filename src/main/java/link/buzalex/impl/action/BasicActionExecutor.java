package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.Action;
import link.buzalex.models.message.BotMessage;
import link.buzalex.utils.ActionStackUtils;

public abstract class BasicActionExecutor<T extends Action> extends ActionExecutor<T> {

    @Override
    public ActionStackObject executeAndMoveCursor(ActionStackObject cursor, BotMessage botMessage, UserContext userContext, T action) {
        execute(botMessage, userContext, action);
        return moveCursor(cursor);
    }

    abstract void execute(BotMessage botMessage, UserContext userContext, T action);


    ActionStackObject moveCursor(ActionStackObject cursor) {
        return ActionStackUtils.move(cursor);
    }
}
