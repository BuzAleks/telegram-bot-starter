package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.Action;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.utils.ActionStackUtils;
import org.springframework.stereotype.Component;

@Component
public class ConditionalActionExecutor extends ActionExecutor<ConditionalAction> {

    @Override
    public Class<ConditionalAction> getActionClass() {
        return ConditionalAction.class;
    }


    @Override
    public ActionStackObject executeAndMoveCursor(ActionStackObject cursor, BotMessage botMessage, UserContext userContext, ConditionalAction action) {
        if (action.condition().test(wrap(botMessage, userContext))) {
            ActionsContainer firstAction = action.conditionalActions();
            return new ActionStackObject(cursor.step(), cursor.action(), firstAction);
        } else {
            return ActionStackUtils.move(cursor);
        }
    }

}
