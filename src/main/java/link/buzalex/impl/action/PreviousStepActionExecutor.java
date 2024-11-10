package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.PreviousStepAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class PreviousStepActionExecutor extends ActionExecutor<PreviousStepAction> {
    private final BotItemsHolder stepsHolder;

    public PreviousStepActionExecutor(BotItemsHolder stepsHolder) {
        this.stepsHolder = stepsHolder;
    }

    @Override
    public Class<PreviousStepAction> getActionClass() {
        return PreviousStepAction.class;
    }

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, PreviousStepAction action) {
        Deque<String> stepsHistory = userContext.getStepsHistory();
        stepsHistory.pop();
        String peek = stepsHistory.peek();

        if (peek == null) {
            return null;
        } else {
            BotStep step = stepsHolder.getStep(peek);
            return new ActionCursor(step, step.stepActions(), null);
        }
    }
}
