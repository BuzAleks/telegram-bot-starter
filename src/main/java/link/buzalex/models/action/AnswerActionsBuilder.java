package link.buzalex.models.action;

import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.actions.ExecuteAction;
import link.buzalex.models.actions.FinishStepAction;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStepsChain;

import java.util.function.Function;
import java.util.function.Predicate;

public class AnswerActionsBuilder extends BaseActionsBuilder<AnswerActionsBuilder> {

    AnswerActionsBuilder(BotStepBuilder stepBuilder) {
        super(stepBuilder.stepActions,stepBuilder);
    }

    public ConditionalActionsBuilder<AnswerActionsBuilder> ifTrue(Predicate<UserMessageContainer> condition) {

        ConditionalAction conditionalAction = new ConditionalAction(condition);
        putAction(conditionalAction);

        return new ConditionalActionsBuilder<>(this, conditionalAction);
    }

    public ConditionalActionsBuilder<AnswerActionsBuilder> ifKeyboardPressed(String key) {
        ConditionalAction conditionalAction = new ConditionalAction(s -> key.equals(s.message().text()));
        putAction(conditionalAction);
        return new ConditionalActionsBuilder<>(this, conditionalAction);
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

    public AnswerActionsBuilder putMessageToContext(String key) {
        putAction(new ExecuteAction(s -> s.context().put(key, s.message())));
        return this;
    }

    public AnswerActionsBuilder putMessageToContext(String key, Function<BotMessage, Object> mapFunc) {
        putAction(new ExecuteAction(s -> s.context().put(key, mapFunc.apply(s.message()))));
        return this;
    }

    public AnswerActionsBuilder putMessageTextToContext(String key) {
        return putMessageToContext(key, BotMessage::text);
    }
}
