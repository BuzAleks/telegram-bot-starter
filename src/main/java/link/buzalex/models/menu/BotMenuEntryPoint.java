package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;

import java.util.Map;
import java.util.function.Predicate;

public record BotMenuEntryPoint(
        String name,
        Predicate<BotMessage> selector,
        int order,
        String rootStepName,
        Map<String, BotStep> steps
) {
}
