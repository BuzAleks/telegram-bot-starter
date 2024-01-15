package link.buzalex.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BotActionBuilder {
    private BotAction nextStep;
    private BiFunction<BotMessage, ? super UserContext, BotAction> nextStepFunc;
    private Map<Long, List<BotMessageReply>> replies = new HashMap<>();
    private List<ConditionalActions> conditionalActions = new ArrayList<>();
    private List<Consumer<BotMessage>> peeks = new ArrayList<>();
    private String paramName;
    private String name;
    private boolean finish;

    public BotActionBuilder name(String stepName) {
        this.name = stepName;
        return this;
    }

    public BotActionBuilder nextStep(BotAction nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    public BotActionBuilder finish() {
        this.finish = true;
        return this;
    }

    public BotActionBuilder nextStep(BiFunction<BotMessage, ? super UserContext, BotAction> stepName) {
        this.nextStepFunc = stepName;
        return this;
    }

    public BotActionBuilder message(BotMessageReply message) {
        this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotActionBuilder message(String message) {
        return this.message(new BotMessageReply(message));
    }

    public BotActionBuilder message(BotMessageReply message, Long userId) {
        this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotActionBuilder message(String message, Long userId) {
        return this.message(new BotMessageReply(message), userId);
    }

    public GotAnswer gotAnswer() {
        return this.new GotAnswer();
    }

    public BotAction build() {
        return new BotAction(
                name, replies, conditionalActions, peeks, paramName, nextStep, nextStep.name(), finish
        );
    }

    public class GotAnswer {
        public ConditionalActionsBuilder ifTrue(Predicate<BotMessage> condition) {
            return new ConditionalActionsBuilder(condition);
        }

        public GotAnswer peek(Consumer<BotMessage> consumer) {
            BotActionBuilder.this.peeks.add(consumer);
            return this;
        }

        public BotActionBuilder saveAs(String name) {
            BotActionBuilder.this.paramName = name;
            return BotActionBuilder.this;
        }
    }

    public class ConditionalActionsBuilder {
        private Predicate<BotMessage> condition;
        private BotAction nextStep;
        private Map<Long, List<BotMessageReply>> replies = new HashMap<>();
        private boolean finish;

        ConditionalActionsBuilder(Predicate<BotMessage> condition) {
            this.condition = condition;
        }

        public ConditionalActionsBuilder nextStep(BotAction stepName) {
            this.nextStep = stepName;
            return this;
        }

        public ConditionalActionsBuilder finish() {
            this.finish = true;
            return this;
        }

        public ConditionalActionsBuilder message(BotMessageReply message) {
            this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
            return this;
        }

        public ConditionalActionsBuilder message(String message) {
            return this.message(new BotMessageReply(message));
        }

        public ConditionalActionsBuilder message(BotMessageReply message, Long userId) {
            this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(message);
            return this;
        }

        public ConditionalActionsBuilder message(String message, Long userId) {
            return this.message(new BotMessageReply(message), userId);
        }

        public GotAnswer also() {
            addActions();
            return new GotAnswer();
        }

        public BotAction build() {
            addActions();
            return BotActionBuilder.this.build();
        }

        private void addActions() {
            BotActionBuilder.this.conditionalActions.add(new ConditionalActions(condition, replies, nextStep, nextStep.name(), finish));
        }
    }
}
