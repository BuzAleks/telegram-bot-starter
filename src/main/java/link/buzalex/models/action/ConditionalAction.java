package link.buzalex.models.action;

import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.step.BotStepsChain;

import java.util.List;
import java.util.function.Predicate;

public class ConditionalAction extends BaseStepAction{
    final List<BaseStepAction> conditionalActions;
    final Predicate<UserMessageContainer> condition;
    final BotStepsChain nextStep;
    final boolean finish;

    public ConditionalAction(List<BaseStepAction> conditionalActions, Predicate<UserMessageContainer> condition, BotStepsChain nextStep, boolean finish) {
        this.conditionalActions = conditionalActions;
        this.condition = condition;
        this.nextStep = nextStep;
        this.finish = finish;
    }

    public List<BaseStepAction> getConditionalActions() {
        return conditionalActions;
    }

    public Predicate<UserMessageContainer> getCondition() {
        return condition;
    }

    public BotStepsChain getNextStep() {
        return nextStep;
    }

    public boolean isFinish() {
        return finish;
    }
}
