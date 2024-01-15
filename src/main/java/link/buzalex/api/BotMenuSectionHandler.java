package link.buzalex.api;

import link.buzalex.models.BotAction;
import link.buzalex.models.UserContext;

public interface BotMenuSectionHandler<T extends UserContext> {
    BotAction startStep();

}
