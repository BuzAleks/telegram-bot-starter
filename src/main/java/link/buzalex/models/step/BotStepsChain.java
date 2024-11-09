package link.buzalex.models.step;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.action.BotStepBuilder;

public record BotStepsChain(
        String name,
        ActionsContainer stepActions,
        BotStepsChain nextStep) {

    public BotStep convertToPlainStep() {
        return new BotStep(name, stepActions, nextStep == null ? null : nextStep.name);
    }

    public static BotStepBuilder builder() {
        return new BotStepBuilder();
    }
}
