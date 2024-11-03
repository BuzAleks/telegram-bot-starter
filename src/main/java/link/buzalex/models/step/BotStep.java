package link.buzalex.models.step;

import link.buzalex.models.action.BaseStepAction;

import java.util.List;

public record BotStep(
        String name,
        List<BaseStepAction> stepActions,
        List<BaseStepAction> answerActions,
        String nextStepName) {
}
