package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.BaseStepAction;
import link.buzalex.models.actions.ReuseStepsChainAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import link.buzalex.utils.ActionStackUtils;
import org.springframework.stereotype.Component;

@Component
public class ReuseStepActionExecutor extends ActionExecutor<ReuseStepsChainAction> {
    private final BotItemsHolder stepsHolder;

    public ReuseStepActionExecutor(BotItemsHolder stepsHolder) {
        this.stepsHolder = stepsHolder;
    }

    @Override
    public Class<ReuseStepsChainAction> getActionClass() {
        return ReuseStepsChainAction.class;
    }

    @Override
    public ActionStackObject executeAndMoveCursor(ActionStackObject cursor, BotMessage botMessage, UserContext userContext, ReuseStepsChainAction action) {
        BotStep step = stepsHolder.getStep(action.stepName());
        BaseStepAction first = step.stepActions().getFirst();

        ActionStackObject nextCursor = ActionStackUtils.move(cursor);
        userContext.getStack().push(ActionStackUtils.convert(nextCursor));
        return new ActionStackObject(step, first, null);
    }

}
