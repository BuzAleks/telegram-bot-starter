package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;

import java.util.Map;
import java.util.function.Predicate;

public record MenuSection(
        String name,
        Predicate<BotMessage> selector,
        Integer order,
        String rootStepName,
        Map<String, BotStep> steps
) {
}
