package link.buzalex.models.step;

import link.buzalex.models.action.ExecuteAction;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;

import java.util.function.Function;
import java.util.function.Predicate;

public class AnswerActionsBuilder extends BaseActionsBuilder<AnswerActionsBuilder> {

    AnswerActionsBuilder(BotStepBuilder stepBuilder) {
        super(stepBuilder);
    }

    public ConditionalActionsBuilder<AnswerActionsBuilder> ifTrue(Predicate<UserMessageContainer> condition) {
        return new ConditionalActionsBuilder<>(this, condition);
    }

    public ConditionalActionsBuilder<AnswerActionsBuilder> ifKeyboardPressed(String key) {
        return new ConditionalActionsBuilder<>(this, s -> key.equals(s.message().text()));
    }

    public BotStepsChain finish() {
        stepBuilder.answerActions = this.actions;
        stepBuilder.nextStep = null;
        return stepBuilder.build();
    }

    public BotStepsChain nextStep(BotStepsChain nextStep) {
        stepBuilder.answerActions = this.actions;
        stepBuilder.nextStep = nextStep;
        return stepBuilder.build();
    }

    public AnswerActionsBuilder putMessageToContext(String key) {
        this.actions.add(new ExecuteAction(s -> s.context().put(key, s.message())));
        return this;
    }

    public AnswerActionsBuilder putMessageToContext(String key, Function<BotMessage, Object> mapFunc) {
        this.actions.add(new ExecuteAction(s -> s.context().put(key, mapFunc.apply(s.message()))));
        return this;
    }

    public AnswerActionsBuilder putMessageTextToContext(String key) {
        return putMessageToContext(key, BotMessage::text);
    }
}
