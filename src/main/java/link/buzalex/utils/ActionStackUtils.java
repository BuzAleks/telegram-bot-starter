package link.buzalex.utils;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.BaseStepAction;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.step.BotStep;

public class ActionStackUtils {
    public static ActionStackObject convert(ActionStackItem stack, BotItemsHolder holder) {
        BotStep step = holder.getStep(stack.step());
        BaseStepAction action = step.stepActions().get(stack.action());
        BaseStepAction subAction = null;
        if (action instanceof ConditionalAction conditionalAction) {
            subAction = conditionalAction.conditionalActions().get(stack.subAction());
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
            BaseStepAction next = cursor.step().stepActions().getNext(cursor.action().getName());
            return new ActionStackObject(cursor.step(), next, null);
        } else if (cursor.action() instanceof ConditionalAction conditionalAction) {
            BaseStepAction nextSub = conditionalAction.conditionalActions().getNext(cursor.subAction().getName());
            return new ActionStackObject(cursor.step(), cursor.action(), nextSub);
        }
        return null;
    }
}
