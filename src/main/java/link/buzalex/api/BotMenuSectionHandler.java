package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface BotMenuSectionHandler<T extends UserContext> {
    boolean enterCondition(BotMessage botMessage, T userContext);
    void startStep(BotMessage botMessage, T userContext);
    int order();
}
