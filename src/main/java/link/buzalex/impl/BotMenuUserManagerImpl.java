package link.buzalex.impl;

import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.BotMenuUserManager;
import link.buzalex.api.UserContextInitializer;
import link.buzalex.api.UserContextStorage;
import link.buzalex.models.BotMessage;
import link.buzalex.api.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BotMenuUserManagerImpl implements BotMenuUserManager {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuUserManagerImpl.class);

    private final UserContextStorage<UserContext> userContextStorage;

    private final BotMenuStepProcessor stepProcessor;

    private final UserContextInitializer<UserContext> userContextInitializer;

    public BotMenuUserManagerImpl(UserContextStorage userContextStorage,
                                  BotMenuStepProcessor stepProcessor,
                                  UserContextInitializer userContextInitializer) {
        this.userContextStorage = userContextStorage;
        this.stepProcessor = stepProcessor;
        this.userContextInitializer = userContextInitializer;
    }

    @Override
    public void handleMessage(BotMessage message) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Got message: {}", message.text());
        }

        UserContext user = userContextStorage.getUser(message.userId());
        user = user == null ? userContextInitializer.initUser(message) : user;

        stepProcessor.processStep(message, user);
        userContextStorage.saveUser(user);
    }
}
