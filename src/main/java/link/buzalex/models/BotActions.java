package link.buzalex.models;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record BotActions(
        String nextStep,
        Map<Long, List<BotMessageReply>> replies,
        List<BotActionsBuilder.ConditionalActions> conditionalActions,
        List<Consumer<BotMessage>> peeks,
        String paramName
) {
    public static BotActionsBuilder builder() {
        return new BotActionsBuilder();
    }
}
