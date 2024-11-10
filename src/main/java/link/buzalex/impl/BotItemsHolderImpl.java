package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotItemsHolderImpl implements BotItemsHolder {
    private final Map<String, BotEntryPoint> entryPoints = new HashMap<>();

    private final Map<String, BotStep> botSteps = new HashMap<>();

    private final Map<String, Map<String, ActionsContainer>> botStepsActions = new HashMap<>();

    @Override
    public ActionsContainer getStepAction(String stepName, String actionName, String subActionName) {
        return getStepAction(stepName, subActionName == null ? actionName : actionName + "." + subActionName);
    }

    @Override
    public ActionsContainer getStepAction(String stepName, String actionName) {
        return botStepsActions.get(stepName).get(actionName);
    }

    @Override
    public BotStep getStep(String stepName) {
        return botSteps.get(stepName);
    }

    @Override
    public void putStep(BotStep botStep) {
        ActionsContainer actionsContainer = botStep.stepActions();
        Map<String, ActionsContainer> actionsContainerMap = new HashMap<>();

        while (actionsContainer != null) {
            if (actionsContainer.getAction() instanceof ConditionalAction conditionalAction) {
                Map<String, ActionsContainer> conditionActionsContainerMap = extractActions(conditionalAction, actionsContainer);
                actionsContainerMap.putAll(conditionActionsContainerMap);
            }
            actionsContainerMap.put(actionsContainer.getName(), actionsContainer);
            actionsContainer = actionsContainer.getNextAction();
        }
        botStepsActions.put(botStep.name(), actionsContainerMap);
        botSteps.put(botStep.name(), botStep);
    }

    private static Map<String, ActionsContainer> extractActions(ConditionalAction conditionalAction, ActionsContainer actionsContainer) {
        ActionsContainer conditionActionsContainer = conditionalAction.conditionalActions();
        Map<String, ActionsContainer> conditionActionsContainerMap = new HashMap<>();
        while (conditionActionsContainer != null) {
            conditionActionsContainerMap.put(actionsContainer.getName() + "." + conditionActionsContainer.getName(), conditionActionsContainer);
            conditionActionsContainer = conditionActionsContainer.getNextAction();
        }
        return conditionActionsContainerMap;
    }

    @Override
    public BotEntryPoint getEntryPoint(String entryPointName) {
        return entryPoints.get(entryPointName);
    }

    @Override
    public Map<String, BotEntryPoint> getEntryPoints() {
        return entryPoints;
    }

    @Override
    public void putEntryPoint(BotEntryPoint rootStep) {
        entryPoints.put(rootStep.name(), rootStep);
    }
}