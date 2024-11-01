package link.buzalex.api;

import link.buzalex.models.menu.BotMenuEntryPoint;
import link.buzalex.models.UserContextImpl;

public interface BotMenuSectionProvider<T extends UserContextImpl> {
    BotMenuEntryPoint provideMenuSection();
}
