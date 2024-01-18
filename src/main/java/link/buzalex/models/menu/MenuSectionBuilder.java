package link.buzalex.models.menu;

import link.buzalex.models.BotMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class MenuSectionBuilder {
    private String name;
    private Predicate<BotMessage> selector;
    private int order;
    private String rootStepName;
    private Map<String, BotStep> steps = new HashMap<>();

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
        steps.put(step.name, step.build());
        for (BotStepBuilder.ConditionalActionsBuilder conditionalAction : step.conditionalActions) {
            if (isFinishOrDuplicate(conditionalAction)) continue;
            collect(conditionalAction.nextStep);
        }
        if (isFinishOrDuplicate(step)) return;
        collect(step.nextStep);
    }

    private boolean isFinishOrDuplicate(BotStepBuilder.ConditionalActionsBuilder step) {
        return step.finish || step.nextStep == null || steps.containsKey(step.nextStep.name);
    }

    private boolean isFinishOrDuplicate(BotStepBuilder step) {
        return step.finish || step.nextStep == null || steps.containsKey(step.nextStep.name);
    }

    public MenuSection build() {
        return new MenuSection(name, selector, order, rootStepName, steps);
    }
}
