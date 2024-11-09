package link.buzalex.models.action;

import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.actions.WaitAnswerAction;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.step.BotStepsChain;

import java.util.function.Predicate;

public class StepActionsBuilder extends BaseActionsBuilder<StepActionsBuilder> {

    StepActionsBuilder(BotStepBuilder stepBuilder) {
        super(stepBuilder.stepActions, stepBuilder);
    }

    public ConditionalActionsBuilder<StepActionsBuilder> ifTrue(Predicate<UserMessageContainer> condition) {
        ConditionalAction conditionalAction = new ConditionalAction(condition);
        putAction(conditionalAction);
        return new ConditionalActionsBuilder<>(this, conditionalAction);
    }

    public AnswerActionsBuilder waitAnswer() {
        putAction(new WaitAnswerAction());
        return new AnswerActionsBuilder(stepBuilder);
    }

    public BotStepsChain finish() {
        putAction(new FinishStepAction());
        stepBuilder.nextStep = null;
        return stepBuilder.build();
    }

    public BotStepsChain nextStep(BotStepsChain nextStep) {
        putAction(new FinishStepAction(nextStep.name()));
        stepBuilder.nextStep = nextStep;
        return stepBuilder.build();
    }
}
