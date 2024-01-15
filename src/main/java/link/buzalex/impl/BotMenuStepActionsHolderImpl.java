package link.buzalex.impl;

import link.buzalex.api.BotMenuStepActionsHolder;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.models.BotAction;
import link.buzalex.models.RootBotAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotMenuStepActionsHolderImpl implements BotMenuStepActionsHolder {

    Map<String, Map<String, BotAction>> actionsMap = new HashMap<>();

    List<RootBotAction> rootBotActions = new ArrayList<>();


    @Override
    public BotAction getStepActions(String menuSection, String stepName) {
        final Map<String, BotAction> particularClass = actionsMap.get(menuSection);
        if (particularClass == null) {
            throw new BotMenuStepMethodException("Couldn't find actions for class name " + menuSection);
        }
        final BotAction particularActions = particularClass.get(stepName);
        if (particularActions == null) {
            throw new BotMenuStepMethodException("Couldn't find method " + stepName + " for class name " + menuSection);
        }
        return particularActions;
    }

    @Override
    public void putStepActions(String menuSection, String stepName, BotAction actions) {

        actionsMap.computeIfAbsent(menuSection, s -> new HashMap<>()).put(stepName, actions);
        System.out.println("Added " + menuSection + " " + stepName + " " + actions);
    }

    @Override
    public List<RootBotAction> getRootActions() {
        return rootBotActions;
    }
}
