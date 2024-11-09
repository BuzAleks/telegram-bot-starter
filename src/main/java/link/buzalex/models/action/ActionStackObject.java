package link.buzalex.models.action;

import link.buzalex.models.actions.BaseStepAction;
import link.buzalex.models.step.BotStep;

public record ActionStackObject(BotStep step, BaseStepAction action, BaseStepAction subAction) {

}
