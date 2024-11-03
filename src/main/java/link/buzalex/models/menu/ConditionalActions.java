package link.buzalex.models.menu;

import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalActions extends BaseStepActions {
    private final Predicate<UserMessageContainer> condition;
    private final BotStepsChain nextStep;

    public ConditionalActions(Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies, List<Consumer<UserMessageContainer>> peeks, Predicate<UserMessageContainer> condition, BotStepsChain nextStep, boolean clearLastMessage) {
        super(replies, peeks, clearLastMessage);
        this.condition = condition;
        this.nextStep = nextStep;
    }

    public Predicate<UserMessageContainer> condition() {
        return condition;
    }

    public BotStepsChain nextStep() {
        return nextStep;
    }

}
