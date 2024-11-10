package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.WaitAnswerAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class WaitAnswerActionExecutor extends ActionExecutor<WaitAnswerAction> {
    @Override
    public Class<WaitAnswerAction> getActionClass() {
        return WaitAnswerAction.class;
    }

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, WaitAnswerAction action) {
        ActionCursor nextCursor = moveCursor(cursor);
        userContext.getStack().push(convert(nextCursor));
        return null;
    }

}
