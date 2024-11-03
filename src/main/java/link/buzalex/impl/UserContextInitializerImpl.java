package link.buzalex.impl;

import link.buzalex.api.UserContext;
import link.buzalex.api.UserContextInitializer;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.context.UserContextImpl;
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
        final UserContext userContext = new UserContextImpl();
        userContext.setId(botMessage.userId());
        LOG.debug("New user initialized: " + botMessage.userId());
        return userContext;
    }
}
