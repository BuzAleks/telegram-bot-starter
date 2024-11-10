package link.buzalex.models.action;

import link.buzalex.models.step.BotStepsChain;

public class BotStepBuilder {
    String name;
    BotStepsChain nextStep;
    ActionsContainer stepActions = new ActionsContainer();

    public BotStepBuilder() {
    }

    BotStepsChain build() {
        return new BotStepsChain(name, stepActions, nextStep);
    }

    public StepActionsBuilder name(String stepName) {
        this.name = stepName;
        return new StepActionsBuilder(this);
    }
}
