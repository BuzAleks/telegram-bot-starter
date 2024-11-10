package link.buzalex.utils;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.step.BotStep;

public class ActionStackUtils {
    public static ActionStackObject convert(ActionStackItem stack, BotItemsHolder holder) {
        BotStep step = holder.getStep(stack.step());
        ActionsContainer action = holder.getStepAction(step.name(), stack.action());
        ActionsContainer subAction = null;
        if (action.getAction() instanceof ConditionalAction) {
            subAction = holder.getStepAction(step.name(), stack.action(), stack.subAction());
        }
        return new ActionStackObject(step, action, subAction);
    }

    public static ActionStackItem convert(ActionStackObject stack) {
        String stepName = stack.step().name();
        String actionName = stack.action() == null ? null : stack.action().getName();
        String subActionNames = stack.subAction() == null ? null : stack.subAction().getName();
        return new ActionStackItem(stepName, actionName, subActionNames);
    }

    public static ActionStackObject move(ActionStackObject cursor) {
        if (cursor == null) return null;
        if (cursor.subAction() == null) {
            ActionsContainer next = cursor.action().getNextAction();
            return new ActionStackObject(cursor.step(), next, null);
        } else if (cursor.action().getAction() instanceof ConditionalAction) {
            ActionsContainer nextSub = cursor.subAction().getNextAction();
            return new ActionStackObject(cursor.step(), cursor.action(), nextSub);
        }
        return null;
    }
}
