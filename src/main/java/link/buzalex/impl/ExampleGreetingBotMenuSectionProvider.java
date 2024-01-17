package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.models.UserContext;
import link.buzalex.models.menu.BotStepBuilder;
import link.buzalex.models.menu.MenuSection;
import link.buzalex.models.menu.MenuSectionBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionProvider.class, ignored = ExampleGreetingBotMenuSectionProvider.class)
public class ExampleGreetingBotMenuSectionProvider implements BotMenuSectionProvider<UserContext> {

    @Override
    public MenuSection provideMenuSection() {
        // TODO: 16.01.2024 Add message with passing function(message,user)
        // TODO: 16.01.2024 Add keyboard row and col parameters
        return MenuSectionBuilder
                .name("first")
                .steps(nextStep())
                .build();
    }

    public BotStepBuilder nextStep() {
        return BotStepBuilder
                .name("nextStep")
                .message("Hello, !")
                .finish();
    }
}
