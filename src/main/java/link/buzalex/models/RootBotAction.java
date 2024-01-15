package link.buzalex.models;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RootBotAction extends BotAction {
    private final String sectionName;
    private final Predicate<BotMessage> selector;
    private final Integer order;

    public RootBotAction(String name, Predicate<BotMessage> selector, Integer order, Map<Long, List<BotMessageReply>> replies, List<ConditionalActions> conditionalActions, List<Consumer<BotMessage>> peeks, String saveAs, BotAction nextStep, String nextStepName, boolean finish, String sectionName) {
        super(name, replies, conditionalActions, peeks, saveAs, nextStep, nextStepName, finish);
        this.selector = selector;
        this.order = order;
        this.sectionName = sectionName;
    }

    public Predicate<BotMessage> getSelector() {
        return selector;
    }

    public Integer getOrder() {
        return order;
    }

    public String getSectionName() {
        return sectionName;
    }
}
