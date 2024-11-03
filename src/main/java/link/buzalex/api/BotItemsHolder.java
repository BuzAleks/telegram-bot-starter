package link.buzalex.api;

import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.menu.BotStep;

import java.util.Map;

public interface BotItemsHolder {
    BotStep getStep(String stepName);

    void putStep(BotStep rootStep);

    BotEntryPoint getEntryPoint(String entryPointName);

    Map<String, BotEntryPoint> getEntryPoints();

    void putEntryPoint(BotEntryPoint rootStep);
}
