package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.models.BotAction;
import link.buzalex.models.UserContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionHandler.class, ignored = ExampleGreetingBotMenuSectionHandler.class)
public class ExampleGreetingBotMenuSectionHandler implements BotMenuSectionHandler<UserContext> {

    @Override
    public BotAction startStep() {
        // TODO: 16.01.2024 Add RootBotActionBuilder with sectionName, selector and order
        // TODO: 16.01.2024 Add message with passing function(message,user)
        // TODO: 16.01.2024 Add keyboard row and col parameters
        return BotAction.builder()
                .name("first")
                .message("What's your name?")
                .gotAnswer()
                .ifTrue(mes -> Objects.equals(mes.text(), "Alex"))
                .message("Hi Alex")
                .nextStep(lastStep())
                .also()
                .peek(mes -> System.out.println(mes.chatId()))
                .saveAs("name")
                .nextStep(this.nextStep())
                .build();
    }

    public BotAction nextStep() {
        return BotAction.builder()
                .name("nextStep")
                .message("Hello, !")
                .finish()
                .build();
    }

    public BotAction lastStep() {
        return BotAction.builder()
                .name("LastStep")
                .message("Lol")
                .finish()
                .build();
    }
}
