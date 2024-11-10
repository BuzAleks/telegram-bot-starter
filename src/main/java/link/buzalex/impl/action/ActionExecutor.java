package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.actions.Action;
import link.buzalex.models.context.UserContextWrapper;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;

public abstract class ActionExecutor<T extends Action> {
    public abstract Class<T> getActionClass();

    public abstract ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, T action);

    UserMessageContainer wrap(BotMessage botMessage, UserContext userContext) {
        UserContextWrapper userContextWrapper = new UserContextWrapper(userContext);
        return new UserMessageContainer(botMessage, userContextWrapper);
    }

    ActionCursor convert(ActionStackItem stack, BotItemsHolder holder) {
        return ActionCursor.fromStack(stack, holder);
    }

    ActionStackItem convert(ActionCursor cursor) {
        if (cursor == null) return null;
        return cursor.convertToStack();
    }

    ActionCursor moveCursor(ActionCursor cursor) {
        if (cursor == null) return null;
        return cursor.move();
    }
}
