package link.buzalex.models.action;

import link.buzalex.models.step.BotStep;

public record ActionStackObject(BotStep step, ActionsContainer action, ActionsContainer subAction) {

}
