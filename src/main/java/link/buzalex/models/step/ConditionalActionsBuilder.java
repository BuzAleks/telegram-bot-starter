package link.buzalex.models.step;

import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.action.ConditionalAction;

import java.util.function.Predicate;

public class ConditionalActionsBuilder<T extends BaseActionsBuilder<T>> extends BaseActionsBuilder<ConditionalActionsBuilder<T>> {

    final T parentBuilder;
    final Predicate<UserMessageContainer> condition;

    public ConditionalActionsBuilder(T parentBuilder, Predicate<UserMessageContainer> condition) {
        super();
        this.parentBuilder = parentBuilder;
        this.condition = condition;
    }

    public T finish() {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, true));
        return parentBuilder;
    }

    public T nextStep(BotStepsChain nextStep) {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, nextStep, false));
        return parentBuilder;
    }

    public T endIf() {
        parentBuilder.actions.add(new ConditionalAction(this.actions, condition, null, false));
        return parentBuilder;
    }
}
