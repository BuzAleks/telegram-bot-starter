package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalActions extends BaseStepActions {
    private final Predicate<BotMessage> condition;
    private final String nextStepName;

    public ConditionalActions(Map<Long, List<Function<BotMessage, BotMessageReply>>> replies, List<Consumer<BotMessage>> peeks, Predicate<BotMessage> condition, String nextStepName) {
        super(replies, peeks);
        this.condition = condition;
        this.nextStepName = nextStepName;
    }


    public Predicate<BotMessage> condition() {
        return condition;
    }

    public String nextStepName() {
        return nextStepName;
    }

}
