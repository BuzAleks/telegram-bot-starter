package link.buzalex.models.step;

import link.buzalex.models.action.ActionsContainer;

public record BotStep(
        String name,
        ActionsContainer stepActions) {
}
