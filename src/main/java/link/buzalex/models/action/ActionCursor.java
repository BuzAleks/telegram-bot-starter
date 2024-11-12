package link.buzalex.models.action;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.step.BotStep;

public record ActionCursor(BotStep step, ActionsContainer action, ActionsContainer subAction) {
    public static ActionCursor fromStack(ActionStackItem stack, BotItemsHolder holder) {
        BotStep step = holder.getStep(stack.step());
        ActionsContainer action = holder.getStepAction(step.name(), stack.action());
        ActionsContainer subAction = null;
        if (stack.subAction()!=null && action.getAction() instanceof ConditionalAction) {
            subAction = holder.getStepAction(step.name(), stack.action(), stack.subAction());
        }
        return new ActionCursor(step, action, subAction);
    }

    public ActionStackItem convertToStack() {
        String stepName = this.step().name();
        String actionName = this.action() == null ? null : this.action().getName();
        String subActionNames = this.subAction() == null ? null : this.subAction().getName();
        return new ActionStackItem(stepName, actionName, subActionNames);
    }

    public ActionCursor move() {
        if (this.subAction() == null) {
            ActionsContainer next = this.action().getNextAction();
            return new ActionCursor(this.step(), next, null);
        } else if (this.action().getAction() instanceof ConditionalAction) {
            ActionsContainer nextSub = this.subAction().getNextAction();
            return new ActionCursor(this.step(), this.action(), nextSub);
        }
        return null;
    }

    public String getActionName() {
        String subActionName = this.subAction() == null ? "" : "."+this.subAction().getName();
        return this.action().getName() + subActionName;
    }
}
