package link.buzalex.models.menu;

public record BotStepsChain(
        String name,
        BaseStepActions stepActions,
        AnswerActions answerActions,
        BotStepsChain nextStep) {

    public BotStep convertToPlainStep() {
        return new BotStep(name, stepActions, answerActions, nextStep == null ? null : nextStep.name);
    }

    public static BotStepBuilder.StepActionsBuilder withName(String name){
        return BotStepBuilder.name(name);
    }
}
