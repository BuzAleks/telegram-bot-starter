package link.buzalex.models.actions;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.action.ActionsContainerImpl;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.step.BotStepsChain;

import java.util.Objects;
import java.util.function.Predicate;

public final class ConditionalAction extends BaseStepAction {
    private ActionsContainer conditionalActions = new ActionsContainerImpl();
    private final Predicate<UserMessageContainer> condition;
    private BotStepsChain nextStep;
    private String nextStepName;
    private boolean finish;

    public ConditionalAction(ActionsContainer conditionalActions,
                             Predicate<UserMessageContainer> condition,
                             BotStepsChain nextStep,
                             String nextStepName,
                             boolean finish) {
        this.conditionalActions = conditionalActions;
        this.condition = condition;
        this.nextStep = nextStep;
        this.nextStepName = nextStepName;
        this.finish = finish;
    }

    public ConditionalAction(Predicate<UserMessageContainer> condition) {
        this.condition = condition;
    }

    public void setConditionalActions(ActionsContainer conditionalActions) {
        this.conditionalActions = conditionalActions;
    }

    public void setNextStep(BotStepsChain nextStep) {
        this.nextStep = nextStep;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public ActionsContainer conditionalActions() {
        return conditionalActions;
    }

    public Predicate<UserMessageContainer> condition() {
        return condition;
    }

    public BotStepsChain nextStep() {
        return nextStep;
    }

    public String nextStepName() {
        return nextStepName;
    }

    public boolean finish() {
        return finish;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConditionalAction) obj;
        return Objects.equals(this.conditionalActions, that.conditionalActions) &&
                Objects.equals(this.condition, that.condition) &&
                Objects.equals(this.nextStep, that.nextStep) &&
                Objects.equals(this.nextStepName, that.nextStepName) &&
                this.finish == that.finish;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionalActions, condition, nextStep, nextStepName, finish);
    }

    @Override
    public String toString() {
        return "ConditionalAction[" +
                "name="+name +", "+
                "conditionalActions=" + conditionalActions + ", " +
                "condition=" + condition+"]";
    }
}
