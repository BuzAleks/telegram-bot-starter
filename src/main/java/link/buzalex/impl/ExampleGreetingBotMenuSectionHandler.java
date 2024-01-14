package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import link.buzalex.processor.BotMenuStep;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionHandler.class, ignored = ExampleGreetingBotMenuSectionHandler.class)
public class ExampleGreetingBotMenuSectionHandler implements BotMenuSectionHandler<UserContext> {
    public static final String GREETING_STEP = "greeting_step";

    @Override
    public boolean enterCondition(BotMessage botMessage, UserContext userContext) {
        return "/start".equals(botMessage.text());
    }

    @Override
    public BotActions startStep(BotMessage botMessage, UserContext userContext) {
        return BotActions.builder()
                .message("What's your name?")
                .gotAnswer()
                .saveAs("name")
                .nextStep(GREETING_STEP)
                .build();
    }

    @BotMenuStep(GREETING_STEP)
    public BotActions nextStep(BotMessage botMessage, UserContext userContext) {
        return BotActions.builder()
                .message("Hello, " + userContext.getData().get("name") + "!")
                .nextStep(BotMenuManagerImpl.START_POSITION)
                .build();
    }
}
