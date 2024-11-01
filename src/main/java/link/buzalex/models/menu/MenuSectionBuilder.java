package link.buzalex.models.menu;

import link.buzalex.api.BotMenuStepBuilder;
import link.buzalex.exception.BotMenuStepInitializationException;
import link.buzalex.models.BotMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class MenuSectionBuilder {
    private String name;
    private Predicate<BotMessage> selector;
    private int order;
    private String rootStepName;
    private Map<String, BotStep> steps = new HashMap<>();
    private Set<BotStepBuilder> stepBuiders = new HashSet<>();

    private MenuSectionBuilder(String name) {
        this.name = name;
    }

    public static MenuSectionBuilder name(String name) {
        return new MenuSectionBuilder(name);
    }


    public MenuSectionBuilder selector(Predicate<BotMessage> selector) {
        this.selector = selector;
        return this;
    }

    public MenuSectionBuilder order(int order) {
        this.order = order;
        return this;
    }

    public MenuSectionBuilder steps(BotStepBuilder steps) {
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
        if (stepBuiders.contains(step.getNextStep())) throw new BotMenuStepInitializationException(String.format("Step name '%s' are duplicate", step.getNextStep().name));

        return step.getNextStep() == null || steps.containsKey(step.getNextStep().name);
    }

    public MenuSection build() {
        return new MenuSection(name, selector, order, rootStepName, steps);
    }
}
