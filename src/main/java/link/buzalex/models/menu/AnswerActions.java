package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AnswerActions extends BaseStepActions {
    private final String saveAs;
    private final List<ConditionalActions> conditionalActions;

    public AnswerActions(Map<Long, List<Function<BotMessage, BotMessageReply>>> replies, List<Consumer<BotMessage>> peeks, String saveAs, List<ConditionalActions> conditionalActions) {
        super(replies, peeks);
        this.saveAs = saveAs;
        this.conditionalActions = conditionalActions;
    }

    public String saveAs() {
        return saveAs;
    }

    public List<ConditionalActions> conditionalActions() {
        return conditionalActions;
    }
}
