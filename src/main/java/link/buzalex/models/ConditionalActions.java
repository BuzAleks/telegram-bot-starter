package link.buzalex.models;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public record ConditionalActions(
        Predicate<BotMessage> condition,
        Map<Long, List<BotMessageReply>> replies,
        BotAction nextStep,
        String nextStepName,
        boolean finish) {
}
