package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.BaseStepAction;
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
            BaseStepAction firstAction = action.conditionalActions().getFirst();
            return new ActionStackObject(cursor.step(), cursor.action(), firstAction);
        } else {
            return ActionStackUtils.move(cursor);
        }
    }

}
