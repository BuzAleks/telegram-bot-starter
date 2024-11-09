package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.BaseStepAction;
import link.buzalex.models.context.UserContextWrapper;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;

public abstract class ActionExecutor<T extends BaseStepAction> {
    abstract public Class<T> getActionClass();

    UserMessageContainer wrap(BotMessage botMessage, UserContext userContext) {
        UserContextWrapper userContextWrapper = new UserContextWrapper(userContext);
        return new UserMessageContainer(botMessage, userContextWrapper);
    }

    public abstract ActionStackObject executeAndMoveCursor(ActionStackObject cursor, BotMessage botMessage, UserContext userContext, T action);
}
