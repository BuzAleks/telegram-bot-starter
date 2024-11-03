package link.buzalex.models.step;

import link.buzalex.models.action.BaseStepAction;

import java.util.ArrayList;
import java.util.List;

public class BotStepBuilder {
    String name;
    BotStepsChain nextStep;
    List<BaseStepAction> stepActions = new ArrayList<>();
    List<BaseStepAction> answerActions = new ArrayList<>();

    BotStepBuilder() {
    }

    BotStepsChain build() {
        return new BotStepsChain(name, stepActions, answerActions, nextStep);
    }

    public StepActionsBuilder name(String stepName) {
        this.name = stepName;
        return new StepActionsBuilder(this);
    }
}
