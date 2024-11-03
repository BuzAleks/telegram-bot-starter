package link.buzalex.api;

import link.buzalex.models.step.BotStepBuilder;

public interface BotMenuStepBuilder {
    String getNextStepName();
    BotStepBuilder getNextStep();
}
