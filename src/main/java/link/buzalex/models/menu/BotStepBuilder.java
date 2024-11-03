package link.buzalex.models.menu;

import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserMessageContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BotStepBuilder {
    String name;
    BotStepsChain nextStep;
    StepActionsBuilder stepActions;
    AnswerActionsBuilder answerActions;

    BotStepBuilder(String name) {
        this.name = name;
    }

    BotStepsChain build() {
        BaseStepActions stepActionsResult = stepActions == null ? null : stepActions.build();
        AnswerActions answerActionsResult = answerActions == null ? null : answerActions.build();
        return new BotStepsChain(name, stepActionsResult, answerActionsResult, nextStep);
    }

    public static StepActionsBuilder name(String stepName) {
        final BotStepBuilder botStepBuilder = new BotStepBuilder(stepName);
        botStepBuilder.stepActions = botStepBuilder.new StepActionsBuilder();
        return botStepBuilder.stepActions;
    }

    class BaseStepActionsBuilder {
        final Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies = new HashMap<>();
        final List<Consumer<UserMessageContainer>> peeks = new ArrayList<>();

        public BotStepsChain nextStep(BotStepsChain nextStep) {
            BotStepBuilder.this.nextStep = nextStep;
            return build();
        }

        public BotStepsChain finish() {
            BotStepBuilder.this.nextStep = null;
            return build();
        }
    }

    public class StepActionsBuilder extends BaseStepActionsBuilder {

        boolean clearLastMessage;

        StepActionsBuilder() {
        }

        BaseStepActions build() {
            return new BaseStepActions(replies, peeks, clearLastMessage);
        }

        public StepActionsBuilder clearLast() {
            this.clearLastMessage = true;
            return this;
        }

        public StepActionsBuilder message(Long user, Function<UserMessageContainer, BotMessageReply> message) {
            this.replies.computeIfAbsent(user, s -> new ArrayList<>()).add(message);
            return this;
        }

        public StepActionsBuilder message(Function<UserMessageContainer, BotMessageReply> message) {
            this.message(0L, message);
            return this;
        }

        public StepActionsBuilder message(BotMessageReply message) {
            this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(mes -> message);
            return this;
        }

        public StepActionsBuilder message(String message) {
            return this.message(new BotMessageReply(message, null));
        }

        public StepActionsBuilder peek(Consumer<UserMessageContainer> peek) {
            this.peeks.add(peek);
            return this;
        }

        public AnswerActionsBuilder waitAnswer() {
            final AnswerActionsBuilder answerActionsBuilder = BotStepBuilder.this.new AnswerActionsBuilder();
            BotStepBuilder.this.answerActions = answerActionsBuilder;
            return answerActionsBuilder;
        }

    }

    public class AnswerActionsBuilder extends BaseStepActionsBuilder {
        String saveAs;
        final List<ConditionalActionsBuilder> conditionalActions = new ArrayList<>();
        boolean clearLastMessage;

        AnswerActionsBuilder() {
        }

        AnswerActions build() {
            return new AnswerActions(replies, peeks, saveAs,
                    conditionalActions.stream()
                            .map(ConditionalActionsBuilder::build)
                            .collect(Collectors.toList()), clearLastMessage);
        }

        public ConditionalActionsBuilder ifTrue(Predicate<UserMessageContainer> condition) {
            final ConditionalActionsBuilder conditionalActionsBuilder = new ConditionalActionsBuilder(condition);
            this.conditionalActions.add(conditionalActionsBuilder);
            return conditionalActionsBuilder;
        }

        public AnswerActionsBuilder clearLast() {
            this.clearLastMessage = true;
            return this;
        }

        public AnswerActionsBuilder peek(Consumer<UserMessageContainer> consumer) {
            this.peeks.add(consumer);
            return this;
        }

        public AnswerActionsBuilder saveAs(String name) {
            this.saveAs = name;
            return this;
        }

        public class ConditionalActionsBuilder {
            Predicate<UserMessageContainer> condition;
            BotStepsChain nextStep;
            final Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies = new HashMap<>();
            final List<Consumer<UserMessageContainer>> peeks = new ArrayList<>();


            ConditionalActionsBuilder(Predicate<UserMessageContainer> condition) {
                this.condition = condition;
            }

            ConditionalActions build() {
                return new ConditionalActions(replies, peeks, condition, nextStep, clearLastMessage);
            }

            public AnswerActionsBuilder nextStep(BotStepsChain stepName) {
                this.nextStep = stepName;
                return AnswerActionsBuilder.this;
            }

            public AnswerActionsBuilder finish() {
                this.nextStep = null;
                return AnswerActionsBuilder.this;
            }

            public ConditionalActionsBuilder message(Function<UserMessageContainer, BotMessageReply> message) {
                this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(message);
                return this;
            }

            public ConditionalActionsBuilder message(BotMessageReply message) {
                this.replies.computeIfAbsent(0L, s -> new ArrayList<>()).add(mes -> message);
                return this;
            }

            public ConditionalActionsBuilder message(String message) {
                return this.message(new BotMessageReply(message, null));
            }

            public ConditionalActionsBuilder message(BotMessageReply message, Long userId) {
                this.replies.computeIfAbsent(userId, s -> new ArrayList<>()).add(mes -> message);
                return this;
            }

            public ConditionalActionsBuilder message(String message, Long userId) {
                return this.message(new BotMessageReply(message, null), userId);
            }

            public AnswerActionsBuilder endIf() {
                return AnswerActionsBuilder.this;
            }
        }
    }
}
