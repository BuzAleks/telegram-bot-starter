package link.buzalex.models.menu;

import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStepsChain;

import java.util.function.Predicate;

public record BotEntryPoint(
        String name,
        Predicate<BotMessage> selector,
        int order,
        String rootStepName,
        BotStepsChain stepsChain
) {
    public BotEntryPoint convertToPlainEntryPoint() {
        return new BotEntryPoint(name, selector, order, rootStepName == null ? stepsChain.name() == null ? null : stepsChain.name() : rootStepName, null);
    }
}
