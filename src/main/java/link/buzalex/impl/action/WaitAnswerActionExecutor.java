package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.WaitAnswerAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.utils.ActionStackUtils;
import org.springframework.stereotype.Component;

@Component
public class WaitAnswerActionExecutor extends ActionExecutor<WaitAnswerAction> {
    @Override
    public Class<WaitAnswerAction> getActionClass() {
        return WaitAnswerAction.class;
    }

    @Override
    public ActionStackObject executeAndMoveCursor(ActionStackObject cursor, BotMessage botMessage, UserContext userContext, WaitAnswerAction action) {
        ActionStackObject nextCursor = ActionStackUtils.move(cursor);
        userContext.getStack().push(ActionStackUtils.convert(nextCursor));
        return null;
    }

}
