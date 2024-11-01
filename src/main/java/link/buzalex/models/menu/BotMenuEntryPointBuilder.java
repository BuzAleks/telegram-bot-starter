package link.buzalex.models.menu;

import link.buzalex.api.BotMenuStepBuilder;
import link.buzalex.exception.BotMenuStepInitializationException;
import link.buzalex.models.BotMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class BotMenuEntryPointBuilder {
    private String name;
    private Predicate<BotMessage> selector;
    private int order;
    private String rootStepName;
    private Map<String, BotStep> steps = new HashMap<>();
    private Set<BotStepBuilder> stepBuiders = new HashSet<>();

    private BotMenuEntryPointBuilder(String name) {
        this.name = name;
    }

    public static BotMenuEntryPointBuilder name(String name) {
        return new BotMenuEntryPointBuilder(name);
    }


    public BotMenuEntryPointBuilder selector(Predicate<BotMessage> selector) {
        this.selector = selector;
        return this;
    }

    public BotMenuEntryPointBuilder order(int order) {
        this.order = order;
        return this;
    }

    public BotMenuEntryPointBuilder steps(BotStepBuilder steps) {
        collect(steps);
        this.rootStepName = steps.name;
        return this;
    }

    private void collect(BotStepBuilder step) {
        stepBuiders.add(step);
        steps.put(step.name, step.build());
        if (step.answerActions != null) {
            for (BotStepBuilder.AnswerActionsBuilder.ConditionalActionsBuilder conditionalAction : step.answerActions.conditionalActions) {
                if (isFinishOrDuplicate(conditionalAction)) continue;
                collect(conditionalAction.nextStep);
            }
        }
        if (isFinishOrDuplicate(step)) return;
        collect(step.nextStep);
    }

    private boolean isFinishOrDuplicate(BotMenuStepBuilder step) {
        boolean nextStepExists = step.getNextStep() != null;
        boolean nextStepNameAlreadyKnown = steps.containsKey(step.getNextStepName());
        boolean nextStepObjectAlreadyKnown = nextStepExists && stepBuiders.contains(step.getNextStep());
        if (nextStepExists && nextStepNameAlreadyKnown && !nextStepObjectAlreadyKnown)
            throw new BotMenuStepInitializationException(String.format("Step name '%s' are duplicate", step.getNextStepName()));
        return !nextStepExists || nextStepNameAlreadyKnown;
    }

    public BotMenuEntryPoint build() {
        return new BotMenuEntryPoint(name, selector, order, rootStepName, steps);
    }
}
