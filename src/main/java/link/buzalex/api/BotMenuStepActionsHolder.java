package link.buzalex.api;

import link.buzalex.models.BotAction;
import link.buzalex.models.RootBotAction;

import java.util.List;

public interface BotMenuStepActionsHolder {
    BotAction getStepActions(String menuSection, String stepName);

    void putStepActions(String menuSection, String stepName, BotAction actions);

    List<RootBotAction> getRootActions();
}
