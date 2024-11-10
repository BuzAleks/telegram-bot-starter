package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.ReuseStepsChainAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
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
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, ReuseStepsChainAction action) {
        BotStep step = stepsHolder.getStep(action.stepName());
        ActionsContainer first = step.stepActions();

        ActionCursor nextCursor = moveCursor(cursor);
        userContext.getStack().push(convertToStack(nextCursor));
        return new ActionCursor(step, first, null);
    }

}
