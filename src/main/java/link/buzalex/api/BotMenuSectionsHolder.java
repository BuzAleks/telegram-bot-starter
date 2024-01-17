package link.buzalex.api;

import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.MenuSection;

import java.util.Map;

public interface BotMenuSectionsHolder {
    BotStep getStep(String menuSection, String stepName);

    void putMenuSection(MenuSection rootStep);

    Map<String, MenuSection> getMenuSections();
}
