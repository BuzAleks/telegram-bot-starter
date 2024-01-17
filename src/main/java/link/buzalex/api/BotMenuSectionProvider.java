package link.buzalex.api;

import link.buzalex.models.menu.MenuSection;
import link.buzalex.models.UserContext;

public interface BotMenuSectionProvider<T extends UserContext> {
    MenuSection provideMenuSection();
}
