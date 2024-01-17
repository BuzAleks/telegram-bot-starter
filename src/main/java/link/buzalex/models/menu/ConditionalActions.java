package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public record ConditionalActions(
        Predicate<BotMessage> condition,
        Map<Long, List<BotMessageReply>> replies,
        String nextStepName,
        boolean finish) {
}
