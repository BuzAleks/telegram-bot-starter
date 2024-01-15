package link.buzalex.impl;

import link.buzalex.api.*;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BotMenuUserManagerImpl implements BotMenuUserManager {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuUserManagerImpl.class);

    private final UserContextStorage<UserContext> userContextStorage;

    private final BotMenuStepActionsProcessor stepProcessor;

    private final UserContextInitializer<UserContext> userContextInitializer;


    public BotMenuUserManagerImpl(UserContextStorage userContextStorage,
                                  BotMenuStepActionsProcessor stepProcessor,
                                  UserContextInitializer userContextInitializer) {
        this.userContextStorage = userContextStorage;
        this.stepProcessor = stepProcessor;
        this.userContextInitializer = userContextInitializer;

//        menuHandlers.forEach((key, value) ->
//                LOG.info("Got menuHandler: " + key)
//        );
    }

    @Override
    public void handleMessage(BotMessage message) {
        LOG.info("Got message: " + message.text());

        UserContext user = userContextStorage.getUser(message.userId());
        user = user == null ? userContextInitializer.initUser(message) : user;

        stepProcessor.processStep(message, user);
        userContextStorage.saveUser(user);
    }
}
