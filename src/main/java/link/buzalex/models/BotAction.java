package link.buzalex.models;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class BotAction {
    private final String name;
    private final Map<Long, List<BotMessageReply>> replies;
    private final List<ConditionalActions> conditionalActions;
    private final List<Consumer<BotMessage>> peeks;
    private final String saveAs;
    private final BotAction nextStep;
    private final String nextStepName;
    private final boolean finish;

    public BotAction(
            String name,
            Map<Long, List<BotMessageReply>> replies,
            List<ConditionalActions> conditionalActions,
            List<Consumer<BotMessage>> peeks,
            String saveAs,
            BotAction nextStep,
            String nextStepName,
            boolean finish
    ) {
        this.name = name;
        this.replies = replies;
        this.conditionalActions = conditionalActions;
        this.peeks = peeks;
        this.saveAs = saveAs;
        this.nextStep = nextStep;
        this.nextStepName = nextStepName;
        this.finish = finish;
    }

    public static BotActionBuilder builder() {
        return new BotActionBuilder();
    }

    public String name() {
        return name;
    }

    public Map<Long, List<BotMessageReply>> replies() {
        return replies;
    }

    public List<ConditionalActions> conditionalActions() {
        return conditionalActions;
    }

    public List<Consumer<BotMessage>> peeks() {
        return peeks;
    }

    public String saveAs() {
        return saveAs;
    }

    public BotAction nextStep() {
        return nextStep;
    }

    public String nextStepName() {
        return nextStepName;
    }

    public boolean finish() {
        return finish;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BotAction) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.replies, that.replies) &&
                Objects.equals(this.conditionalActions, that.conditionalActions) &&
                Objects.equals(this.peeks, that.peeks) &&
                Objects.equals(this.saveAs, that.saveAs) &&
                Objects.equals(this.nextStep, that.nextStep) &&
                Objects.equals(this.nextStepName, that.nextStepName) &&
                this.finish == that.finish;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, replies, conditionalActions, peeks, saveAs, nextStep, nextStepName, finish);
    }

    @Override
    public String toString() {
        return "BotActions[" +
                "name=" + name + ", " +
                "replies=" + replies + ", " +
                "conditionalActions=" + conditionalActions + ", " +
                "peeks=" + peeks + ", " +
                "saveAs=" + saveAs + ", " +
                "nextStep=" + nextStep + ", " +
                "nextStepName=" + nextStepName + ", " +
                "finish=" + finish + ']';
    }

}
