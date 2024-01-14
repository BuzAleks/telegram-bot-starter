package link.buzalex.api;

import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

public interface BotMenuSectionHandler<T extends UserContext> {
    boolean enterCondition(BotMessage botMessage, T userContext);
    BotActions startStep(BotMessage botMessage, T userContext);

    default int order(){
        return 0;
    }
}
