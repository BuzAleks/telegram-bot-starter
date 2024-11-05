package link.buzalex.models.step;

public class StepActionsBuilder extends BaseActionsBuilder<StepActionsBuilder> {

    public StepActionsBuilder(BotStepBuilder stepBuilder) {
        super(stepBuilder);
    }

    public AnswerActionsBuilder waitAnswer() {
        stepBuilder.stepActions = this.actions;
        return new AnswerActionsBuilder(stepBuilder);
    }

    public BotStepsChain finish() {
        stepBuilder.stepActions = this.actions;
        stepBuilder.nextStep = null;
        return stepBuilder.build();
    }

    public BotStepsChain nextStep(BotStepsChain nextStep) {
        stepBuilder.stepActions = this.actions;
        stepBuilder.nextStep = nextStep;
        return stepBuilder.build();
    }
}
