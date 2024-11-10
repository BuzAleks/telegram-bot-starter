package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.RepeatAnswerWaitingStepAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class RepeatAnswerWaitingActionExecutor extends ActionExecutor<RepeatAnswerWaitingStepAction> {
    private final BotItemsHolder stepsHolder;

    public RepeatAnswerWaitingActionExecutor(BotItemsHolder stepsHolder) {
        this.stepsHolder = stepsHolder;
    }

    @Override
    public Class<RepeatAnswerWaitingStepAction> getActionClass() {
        return RepeatAnswerWaitingStepAction.class;
    }

    @Override
    public ActionCursor executeAndMoveCursor(ActionCursor cursor, BotMessage botMessage, UserContext userContext, RepeatAnswerWaitingStepAction action) {
        return new ActionCursor(cursor.step(), stepsHolder.getStepAction(cursor.step().name(), "waitAnswer"), null);
    }
}
