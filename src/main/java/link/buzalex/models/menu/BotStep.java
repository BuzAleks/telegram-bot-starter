package link.buzalex.models.menu;

public record BotStep(
        String name,
        BaseStepActions stepActions,
        AnswerActions answerActions,
        String nextStepName) {
}
