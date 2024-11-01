package link.buzalex.api;

import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.BotMenuEntryPoint;

import java.util.Map;

public interface BotMenuSectionsHolder {
    BotStep getStep(String menuSection, String stepName);

    void putMenuSection(BotMenuEntryPoint rootStep);

    Map<String, BotMenuEntryPoint> getMenuSections();
}
