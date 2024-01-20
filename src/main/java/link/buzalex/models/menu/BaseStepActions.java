package link.buzalex.models.menu;

import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseStepActions {
    private final Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies;
    private final List<Consumer<UserMessageContainer>> peeks;

    public BaseStepActions(
            Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies,
            List<Consumer<UserMessageContainer>> peeks) {
        this.replies = replies;
        this.peeks = peeks;
    }

    public Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies() {
        return replies;
    }

    public List<Consumer<UserMessageContainer>> peeks() {
        return peeks;
    }
}
