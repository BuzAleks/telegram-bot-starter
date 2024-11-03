package link.buzalex.models.step;

public class AnswerActionsBuilder extends BaseActionsBuilder<AnswerActionsBuilder> {
    private final BotStepBuilder stepBuilder;

    public AnswerActionsBuilder(BotStepBuilder stepBuilder) {
        super();
        this.stepBuilder = stepBuilder;
    }

    public BotStepsChain finish() {
        stepBuilder.answerActions = this.actions;
        stepBuilder.nextStep = null;
        return stepBuilder.build();
    }

    public BotStepsChain nextStep(BotStepsChain nextStep) {
        stepBuilder.answerActions = this.actions;
        stepBuilder.nextStep = nextStep;
        return stepBuilder.build();
    }

    public AnswerActionsBuilder saveAs(String name) {
        return this;
    }
}
