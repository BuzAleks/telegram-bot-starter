package link.buzalex.models.actions;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.step.BotStepsChain;

import java.util.Deque;
import java.util.function.Predicate;

public record ConditionalAction(ActionsContainer conditionalActions,
                                Predicate<UserMessageContainer> condition) implements Action {
}
