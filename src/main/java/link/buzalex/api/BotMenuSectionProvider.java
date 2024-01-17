package link.buzalex.api;

import link.buzalex.models.menu.MenuSection;
import link.buzalex.models.UserContextImpl;

public interface BotMenuSectionProvider<T extends UserContextImpl> {
    MenuSection provideMenuSection();
}
