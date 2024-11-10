package link.buzalex.api;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStep;

import java.util.Map;

public interface BotItemsHolder {
    ActionsContainer getStepAction(String stepName, String actionName, String subActionName);

    ActionsContainer getStepAction(String stepName, String actionName);

    BotStep getStep(String stepName);

    void putStep(BotStep rootStep);

    BotEntryPoint getEntryPoint(String entryPointName);

    Map<String, BotEntryPoint> getEntryPoints();

    void putEntryPoint(BotEntryPoint rootStep);
}
