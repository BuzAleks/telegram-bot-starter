package link.buzalex.models.step;

import link.buzalex.models.context.UserMessageContainer;

import java.util.function.Predicate;

public class StepActionsBuilder extends BaseActionsBuilder<StepActionsBuilder> {

    StepActionsBuilder(BotStepBuilder stepBuilder) {
        super(stepBuilder);
    }

    public ConditionalActionsBuilder<StepActionsBuilder> ifTrue(Predicate<UserMessageContainer> condition) {
        return new ConditionalActionsBuilder<>(this, condition);
    }

    public AnswerActionsBuilder waitAnswer() {
        stepBuilder.stepActions = this.actions;
        return new AnswerActionsBuilder(stepBuilder);
    }

    public BotStepsChain finish() {
        stepBuilder.stepActions = this.actions;
        stepBuilder.nextStep = null;
        return stepBuilder.build();
    }

    public BotStepsChain nextStep(BotStepsChain nextStep) {
        stepBuilder.stepActions = this.actions;
        stepBuilder.nextStep = nextStep;
        return stepBuilder.build();
    }
}
