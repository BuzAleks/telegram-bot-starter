package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.MenuSection;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotMenuSectionsHolderImpl implements BotMenuSectionsHolder {
    Map<String, MenuSection> rootBotActions = new HashMap<>();


    @Override
    public BotStep getStep(String menuSection, String stepName) {
        final MenuSection rootStep = rootBotActions.get(menuSection);
        if (rootStep == null) {
            throw new BotMenuStepMethodException("Couldn't find actions for class name " + menuSection);
        }
        final BotStep botStep = rootStep.steps().get(stepName);
        if (botStep == null) {
            throw new BotMenuStepMethodException("Couldn't find method " + stepName + " for class name " + menuSection);
        }
        return botStep;
    }

    @Override
    public void putMenuSection(MenuSection menuSection) {
        rootBotActions.put(menuSection.name(), menuSection);
    }

    @Override
    public Map<String, MenuSection> getMenuSections() {
        return rootBotActions;
    }
}
