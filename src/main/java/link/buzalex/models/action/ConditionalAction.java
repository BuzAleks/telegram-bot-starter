package link.buzalex.models.action;

import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.step.BotStepsChain;

import java.util.List;
import java.util.function.Predicate;

public record ConditionalAction(List<BaseStepAction> conditionalActions,
                                Predicate<UserMessageContainer> condition,
                                BotStepsChain nextStep,
                                boolean finish) implements BaseStepAction {
}
