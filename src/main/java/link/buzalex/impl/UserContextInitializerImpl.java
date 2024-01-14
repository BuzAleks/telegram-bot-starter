package link.buzalex.impl;

import link.buzalex.api.UserContextInitializer;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(value = UserContextInitializer.class, ignored = UserContextInitializerImpl.class)
public class UserContextInitializerImpl implements UserContextInitializer<UserContext> {
    private static final Logger LOG = LoggerFactory.getLogger(UserContextInitializerImpl.class);

    @Override
    public UserContext initUser(BotMessage botMessage) {
        final UserContext userContext = new UserContext();
        userContext.setId(botMessage.userId());
        userContext.setCurrentStep(BotMenuManagerImpl.START_STEP);
        LOG.info("User initiated: " + botMessage.userId());
        return userContext;
    }
}
