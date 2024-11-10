package link.buzalex.utils;

import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.actions.Action;

import java.util.HashSet;
import java.util.Set;

public class ActionsContainerHelper {
    final ActionsContainer stepActions;
    final Set<String> actionNames = new HashSet<>();

    public ActionsContainerHelper(ActionsContainer stepActions) {
        this.stepActions = stepActions;
        ActionsContainer container = stepActions;
        while (container != null) {
            if (container.getAction() != null && container.getName() != null) {
                actionNames.add(container.getName());
            }
            container = container.getNextAction();
        }
    }

    public void putAction(Action action, String methodName) {
        if (action == null) return;
        if (stepActions.getAction() == null) {
            stepActions.setName(methodName);
            stepActions.setAction(action);
        } else {
            ActionsContainer container = stepActions;
            while (container.getNextAction() != null) {
                container = container.getNextAction();
            }
            methodName = generateUniqueMethodName(methodName);
            container.setNextAction(new ActionsContainer(methodName, action));
        }
        actionNames.add(methodName);
    }

    private String generateUniqueMethodName(String baseName) {
        if (!actionNames.contains(baseName)) {
            return baseName;
        }
        for (int i = 1; i < 100; i++) {
            String newName = baseName + "#" + i;
            if (!actionNames.contains(newName)) {
                return newName;
            }
        }
        return baseName + "#unique";
    }

    Set<String> getActionNames() {
        return actionNames;
    }
}
