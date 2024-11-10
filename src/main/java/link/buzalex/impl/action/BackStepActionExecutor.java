package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.BackStepAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

@Component
public class BackStepActionExecutor extends ActionExecutor<BackStepAction> {
    private final BotItemsHolder stepsHolder;

    public BackStepActionExecutor(BotItemsHolder stepsHolder) {
        this.stepsHolder = stepsHolder;
    }

    @Override
    public Class<BackStepAction> getActionClass() {
        return BackStepAction.class;
    }

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, BackStepAction action) {
        String prevStep = userContext.getStepsHistory().pop();
        BotStep step = stepsHolder.getStep(prevStep);
        return new ActionCursor(step, step.stepActions(), null);
    }
}
