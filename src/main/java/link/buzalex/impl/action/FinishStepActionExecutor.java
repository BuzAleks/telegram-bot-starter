package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

@Component
public class FinishStepActionExecutor extends ActionExecutor<FinishStepAction> {
    private final BotItemsHolder stepsHolder;

    public FinishStepActionExecutor(BotItemsHolder stepsHolder) {
        this.stepsHolder = stepsHolder;
    }

    @Override
    public Class<FinishStepAction> getActionClass() {
        return FinishStepAction.class;
    }

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, FinishStepAction action) {

        String peek = userContext.getStepsHistory().peek();
        if (!cursor.step().name().equals(peek)){
            userContext.getStepsHistory().push(cursor.step().name());
        } else {
            userContext.getStepsHistory().pop();
        }

        if (action.nextStep() == null) {
            if (userContext.getStack().isEmpty()) {
                userContext.setEntryPoint(null);
                userContext.getStepsHistory().clear();
                return null;
            } else {
                ActionStackItem pop = userContext.getStack().pop();
                return convert(pop, stepsHolder);
            }
        } else {
            BotStep step = stepsHolder.getStep(action.nextStep());
            return new ActionCursor(step, step.stepActions(), null);
        }
    }
}
