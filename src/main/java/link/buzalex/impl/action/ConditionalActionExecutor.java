package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class ConditionalActionExecutor extends ActionExecutor<ConditionalAction> {

    @Override
    public Class<ConditionalAction> getActionClass() {
        return ConditionalAction.class;
    }


    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, ConditionalAction action) {
        if (action.condition().test(wrap(botMessage, userContext))) {
            ActionsContainer firstAction = action.conditionalActions();
            return new ActionCursor(cursor.step(), cursor.action(), firstAction);
        } else {
            return moveCursor(cursor);
        }
    }

}
