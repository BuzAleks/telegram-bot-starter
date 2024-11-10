package link.buzalex.impl.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import link.buzalex.utils.ActionStackUtils;
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
    public ActionStackObject executeAndMoveCursor(ActionStackObject cursor,BotMessage botMessage, UserContext userContext, FinishStepAction action) {
        if (action.nextStep() == null) {
            if (userContext.getStack().isEmpty()){
                userContext.setEntryPoint(null);
                return null;
            } else {
                ActionStackItem pop = userContext.getStack().pop();
                return ActionStackUtils.convert(pop, stepsHolder);
            }
        } else {
            BotStep step = stepsHolder.getStep(action.nextStep());
            return new ActionStackObject(step, step.stepActions(), null);
        }
    }
}
