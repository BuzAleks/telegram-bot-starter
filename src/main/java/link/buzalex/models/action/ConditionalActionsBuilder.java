package link.buzalex.models.action;

import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.step.BotStepsChain;

public class ConditionalActionsBuilder<T extends BaseActionsBuilder<T>> extends BaseActionsBuilder<ConditionalActionsBuilder<T>> {

    final T parentBuilder;
    final ConditionalAction condition;

    ConditionalActionsBuilder(T parentBuilder, ConditionalAction condition) {
        super(condition.conditionalActions(), parentBuilder.stepBuilder);
        this.parentBuilder = parentBuilder;
        this.condition = condition;
    }

    public T finish() {
        putAction(new FinishStepAction(null));
        return parentBuilder;
    }

    public T nextStep(BotStepsChain nextStep) {
        putAction(new FinishStepAction(nextStep.name()));
        return parentBuilder;
    }

    public T nextStep(String nextStepName) {
        putAction(new FinishStepAction(nextStepName));
        return parentBuilder;
    }

    public T endIf() {
        return parentBuilder;
    }

    public T repeatCurrentStep() {
        putAction(new FinishStepAction(stepBuilder.name));
        return parentBuilder;
    }
}
