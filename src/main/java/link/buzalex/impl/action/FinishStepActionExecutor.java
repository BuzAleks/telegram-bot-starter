package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

import java.util.Deque;

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
        if (action.nextStep() == null) {
            if (userContext.getStack().isEmpty()) {
                userContext.setEntryPoint(null);
                userContext.getStepsHistory().clear();
                return null;
            } else {
                ActionStackItem pop = userContext.getStack().pop();
                pushStepHistory(pop.step(), userContext.getStepsHistory());
                return convertToCursor(pop, stepsHolder);
            }
        } else {
            BotStep step = stepsHolder.getStep(action.nextStep());
            pushStepHistory(step.name(), userContext.getStepsHistory());

            return new ActionCursor(step, step.stepActions(), null);
        }
    }

    private void pushStepHistory(String stepName, Deque<String> history){
        if (!stepName.equals(history.peek())){
            history.push(stepName);
        }
    }
}
