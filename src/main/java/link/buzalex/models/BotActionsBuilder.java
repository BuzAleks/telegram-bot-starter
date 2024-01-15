package link.buzalex.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BotActionsBuilder {
    String nextStep;
    BiFunction<BotMessage, ? super UserContext, BotActions> nextStepFunc;
    Map<Long, List<BotMessageReply>> replies = new HashMap<>();
    List<ConditionalActions> conditionalActions = new ArrayList<>();
    List<Consumer<BotMessage>> peeks = new ArrayList<>();
    String paramName;
    boolean finish;

    public BotActionsBuilder nextStep(String stepName) {
        this.nextStep = stepName;
        return this;
    }

    public BotActionsBuilder finish() {
        this.finish = true;
        return this;
    }

    public BotActionsBuilder nextStep(BiFunction<BotMessage, ? super UserContext, BotActions> stepName) {
        this.nextStepFunc = stepName;
        return this;
    }

    public BotActionsBuilder message(BotMessageReply message) {
        this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotActionsBuilder message(String message) {
        return this.message(new BotMessageReply(message));
    }

    public BotActionsBuilder message(BotMessageReply message, Long userId) {
        this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotActionsBuilder message(String message, Long userId) {
        return this.message(new BotMessageReply(message), userId);
    }

    public GotAnswer gotAnswer() {
        return this.new GotAnswer();
    }

    public BotActions build() {
        return new BotActions(
                nextStep, replies, conditionalActions, peeks, paramName, nextStepFunc, finish
        );
    }

    public class GotAnswer {
        public ConditionalActions ifTrue(Predicate<BotMessage> condition) {
            return new ConditionalActions(condition);
        }

        public GotAnswer peek(Consumer<BotMessage> consumer) {
            BotActionsBuilder.this.peeks.add(consumer);
            return this;
        }

        public BotActionsBuilder saveAs(String name) {
            BotActionsBuilder.this.paramName = name;
            return BotActionsBuilder.this;
        }
    }


    public class ConditionalActions {
        Predicate<BotMessage> condition;
        String nextStep;
        Map<Long, List<BotMessageReply>> replies = new HashMap<>();
        boolean finish;

        public boolean isFinish() {
            return finish;
        }

        public Predicate<BotMessage> getCondition() {
            return condition;
        }

        public String getNextStep() {
            return nextStep;
        }

        public Map<Long, List<BotMessageReply>> getReplies() {
            return replies;
        }

        ConditionalActions(Predicate<BotMessage> condition) {
            this.condition = condition;
        }

        public ConditionalActions nextStep(String stepName) {
            this.nextStep = stepName;
            return this;
        }

        public ConditionalActions finish() {
            this.finish = true;
            return this;
        }

        public ConditionalActions message(BotMessageReply message) {
            this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
            return this;
        }

        public ConditionalActions message(String message) {
            return this.message(new BotMessageReply(message));
        }

        public ConditionalActions message(BotMessageReply message, Long userId) {
            this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(message);
            return this;
        }

        public ConditionalActions message(String message, Long userId) {
            return this.message(new BotMessageReply(message), userId);
        }

        public GotAnswer also() {
            BotActionsBuilder.this.conditionalActions.add(this);
            return new GotAnswer();
        }

        public BotActions build() {
            BotActionsBuilder.this.conditionalActions.add(this);
            return BotActionsBuilder.this.build();
        }
    }
}
