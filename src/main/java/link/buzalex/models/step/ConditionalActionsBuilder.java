package link.buzalex.models.step;

import link.buzalex.models.action.ConditionalAction;
import link.buzalex.models.context.UserMessageContainer;

import java.util.function.Predicate;

public class ConditionalActionsBuilder<T extends BaseActionsBuilder<T>> extends BaseActionsBuilder<ConditionalActionsBuilder<T>> {

    final T parentBuilder;
    final Predicate<UserMessageContainer> condition;

    public ConditionalActionsBuilder(T parentBuilder, Predicate<UserMessageContainer> condition) {
        super(parentBuilder.stepBuilder);
        this.parentBuilder = parentBuilder;
        this.condition = condition;
    }

    public T finish() {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, null, true));
        return parentBuilder;
    }

    public T nextStep(BotStepsChain nextStep) {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, nextStep, null, false));
        return parentBuilder;
    }

    public T nextStep(String nextStepName) {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, nextStepName, false));
        return parentBuilder;
    }

    public T endIf() {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, null, false));
        return parentBuilder;
    }

    public T repeatCurrentStep() {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, stepBuilder.name, false));
        return parentBuilder;
    }
}
