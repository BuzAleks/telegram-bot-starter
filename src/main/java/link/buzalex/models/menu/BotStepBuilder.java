package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BotStepBuilder {
    BotStepBuilder nextStep;
    Map<Long, List<BotMessageReply>> replies = new HashMap<>();
    List<ConditionalActionsBuilder> conditionalActions = new ArrayList<>();
    List<Consumer<BotMessage>> peeks = new ArrayList<>();
    String paramName;
    String name;
    boolean finish;

    private BotStepBuilder(String name) {
        this.name = name;
    }

    public static BotStepBuilder name(String stepName) {
        return new BotStepBuilder(stepName);
    }

    public BotStepBuilder nextStep(BotStepBuilder nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    public BotStepBuilder finish() {
        this.finish = true;
        return this;
    }

    public BotStepBuilder message(BotMessageReply message) {
        this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotStepBuilder message(String message) {
        return this.message(new BotMessageReply(message));
    }

    public BotStepBuilder message(BotMessageReply message, Long userId) {
        this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(message);
        return this;
    }

    public BotStepBuilder message(String message, Long userId) {
        return this.message(new BotMessageReply(message), userId);
    }

    public GotAnswer gotAnswer() {
        return this.new GotAnswer();
    }

    BotStep build() {
        final List<ConditionalActions> conditionals = conditionalActions.stream()
                .map(s -> new ConditionalActions(s.condition, s.replies, s.nextStep.name, finish))
                .collect(Collectors.toList());
        return new BotStep(name, replies, conditionals, peeks, paramName, nextStep == null ? null : nextStep.name, finish);
    }

    public class GotAnswer {
        public ConditionalActionsBuilder ifTrue(Predicate<BotMessage> condition) {
            return new ConditionalActionsBuilder(condition);
        }

        public GotAnswer peek(Consumer<BotMessage> consumer) {
            BotStepBuilder.this.peeks.add(consumer);
            return this;
        }

        public BotStepBuilder saveAs(String name) {
            BotStepBuilder.this.paramName = name;
            return BotStepBuilder.this;
        }
    }

    public class ConditionalActionsBuilder {
        Predicate<BotMessage> condition;
        BotStepBuilder nextStep;
        Map<Long, List<BotMessageReply>> replies = new HashMap<>();
        boolean finish;

        ConditionalActionsBuilder(Predicate<BotMessage> condition) {
            this.condition = condition;
        }

        public ConditionalActionsBuilder nextStep(BotStepBuilder stepName) {
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

        BotStep build() {
            addActions();
            return BotStepBuilder.this.build();
        }

        private void addActions() {
            BotStepBuilder.this.conditionalActions.add(this);
        }
    }
}
