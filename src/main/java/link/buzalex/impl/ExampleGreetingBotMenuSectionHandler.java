package link.buzalex.impl;

import link.buzalex.api.AbstractBotMenuSectionHandler;
import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import link.buzalex.processor.BotMenuStep;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
@ConditionalOnMissingBean(value = BotMenuSectionHandler.class, ignored = ExampleGreetingBotMenuSectionHandler.class)
public class ExampleGreetingBotMenuSectionHandler extends AbstractBotMenuSectionHandler<UserContext> {
    public static final String STEP = "greeting_step";

    @Override
    public boolean enterCondition(BotMessage botMessage, UserContext userContext) {
        return "/start".equals(botMessage.text());
    }

    @Override
    public void startStep(BotMessage botMessage, UserContext userContext) {
        sendToUser("What's your name?", userContext.getId(), ParseMode.MARKDOWN);
        userContext.setCurrentStep(STEP);
    }

    @BotMenuStep(STEP)
    public void nextStep(BotMessage botMessage, UserContext userContext) {
        sendToUser("Hello, " + botMessage.text(), userContext.getId(), ParseMode.MARKDOWN);
    }
}
