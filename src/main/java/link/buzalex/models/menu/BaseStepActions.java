package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseStepActions {
    private final Map<Long, List<Function<BotMessage, BotMessageReply>>> replies;
    private final List<Consumer<BotMessage>> peeks;

    public BaseStepActions(
            Map<Long, List<Function<BotMessage, BotMessageReply>>> replies,
            List<Consumer<BotMessage>> peeks) {
        this.replies = replies;
        this.peeks = peeks;
    }

    public Map<Long, List<Function<BotMessage, BotMessageReply>>> replies() {
        return replies;
    }

    public List<Consumer<BotMessage>> peeks() {
        return peeks;
    }
}
