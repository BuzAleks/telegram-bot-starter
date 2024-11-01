package link.buzalex.api;

import link.buzalex.models.menu.BotStepBuilder;

public interface BotMenuStepBuilder {
    String getNextStepName();
    BotStepBuilder getNextStep();
}
