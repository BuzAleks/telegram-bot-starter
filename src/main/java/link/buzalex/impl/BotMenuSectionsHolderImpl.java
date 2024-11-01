package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.BotMenuEntryPoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotMenuSectionsHolderImpl implements BotMenuSectionsHolder {
    Map<String, BotMenuEntryPoint> rootBotActions = new HashMap<>();


    @Override
    public BotStep getStep(String menuSection, String stepName) {
        final BotMenuEntryPoint rootStep = rootBotActions.get(menuSection);
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
    public void putMenuSection(BotMenuEntryPoint botMenuEntryPoint) {
        rootBotActions.put(botMenuEntryPoint.name(), botMenuEntryPoint);
    }

    @Override
    public Map<String, BotMenuEntryPoint> getMenuSections() {
        return rootBotActions;
    }
}
