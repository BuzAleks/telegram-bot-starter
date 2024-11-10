package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class BotItemsHolderImpl implements BotItemsHolder {
    private final Map<String, BotEntryPoint> entryPoints = new HashMap<>();
    private final Map<String, BotStep> botSteps = new HashMap<>();
    private final Map<String, Map<String, ActionsContainer>> botStepsActions = new HashMap<>();

    @Override
    public ActionsContainer getStepAction(String stepName, String actionName, String subActionName) {
        return getStepAction(stepName, Optional.ofNullable(subActionName)
                .map(sub -> actionName + "." + sub)
                .orElse(actionName));
    }

    @Override
    public ActionsContainer getStepAction(String stepName, String actionName) {
        return Optional.ofNullable(botStepsActions.get(stepName))
                .map(actions -> actions.get(actionName))
                .orElse(null);
    }

    @Override
    public BotStep getStep(String stepName) {
        return botSteps.get(stepName);
    }

    @Override
    public void putStep(BotStep botStep) {
        Map<String, ActionsContainer> actionsContainerMap = extractActionsRecursively(botStep.stepActions(), null);
        botStepsActions.put(botStep.name(), actionsContainerMap);
        botSteps.put(botStep.name(), botStep);
    }

    private Map<String, ActionsContainer> extractActionsRecursively(ActionsContainer actionsContainer, String parentName) {
        Map<String, ActionsContainer> actionsContainerMap = new HashMap<>();
        while (actionsContainer != null) {
            String actionName = parentName == null ? actionsContainer.getName() : parentName + "." + actionsContainer.getName();
            actionsContainerMap.put(actionName, actionsContainer);
            if (actionsContainer.getAction() instanceof ConditionalAction conditionalAction) {
                actionsContainerMap.putAll(extractActionsRecursively(conditionalAction.conditionalActions(), actionName));
            }
            actionsContainer = actionsContainer.getNextAction();
        }
        return actionsContainerMap;
    }

    @Override
    public BotEntryPoint getEntryPoint(String entryPointName) {
        return entryPoints.get(entryPointName);
    }

    @Override
    public Map<String, BotEntryPoint> getEntryPoints() {
        return Map.copyOf(entryPoints);
    }

    @Override
    public void putEntryPoint(BotEntryPoint rootStep) {
        entryPoints.put(rootStep.name(), rootStep);
    }
}
