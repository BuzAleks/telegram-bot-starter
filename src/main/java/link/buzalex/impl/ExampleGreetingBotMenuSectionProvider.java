package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.models.UserContextImpl;
import link.buzalex.models.menu.BotStepBuilder;
import link.buzalex.models.menu.MenuSection;
import link.buzalex.models.menu.MenuSectionBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionProvider.class, ignored = ExampleGreetingBotMenuSectionProvider.class)
public class ExampleGreetingBotMenuSectionProvider implements BotMenuSectionProvider<UserContextImpl> {

    @Override
    public MenuSection provideMenuSection() {
        // TODO: 16.01.2024 Fix bug with last step (if no need wait for answer and its last step finish it)
        // TODO: 16.01.2024 refactor BotMenuStepProcessor and try to split it to several classes
        // TODO: 16.01.2024 add waitAnswer method with waiting time and do smth if run out (maybe we need timestamp)
        // TODO: 16.01.2024 Try to generify BotStepBuilder to work with UserContext inheritance
        // TODO: 16.01.2024 Add keyboard row and col methods to add keyboard
        // TODO: 16.01.2024 add methods for operating with last message (remove, edit)
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
