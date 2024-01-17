package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record BotStep(
        String name,
        Map<Long, List<BotMessageReply>> replies,
        List<ConditionalActions> conditionalActions,
        List<Consumer<BotMessage>> peeks,
        String saveAs,
        String nextStepName,
        boolean finish) {
}
