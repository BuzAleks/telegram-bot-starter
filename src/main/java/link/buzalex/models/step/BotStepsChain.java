package link.buzalex.models.step;

import link.buzalex.models.action.BaseStepAction;

import java.util.List;

public record BotStepsChain(
        String name,
        List<BaseStepAction> stepActions,
        List<BaseStepAction> answerActions,
        BotStepsChain nextStep) {

    public BotStep convertToPlainStep() {
        return new BotStep(name, stepActions, answerActions, nextStep == null ? null : nextStep.name);
    }

    public static BotStepBuilder builder() {
        return new BotStepBuilder();
    }
}
