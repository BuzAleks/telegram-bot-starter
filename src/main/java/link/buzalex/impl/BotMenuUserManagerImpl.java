package link.buzalex.impl;

import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.BotMenuUserManager;
import link.buzalex.api.UserContextInitializer;
import link.buzalex.api.UserContextStorage;
import link.buzalex.impl.event.BotEventProducer;
import link.buzalex.models.event.BotMessageReceivedEvent;
import link.buzalex.models.message.BotMessage;
import link.buzalex.api.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BotMenuUserManagerImpl implements BotMenuUserManager {
    private final UserContextStorage<UserContext> userContextStorage;

    private final BotMenuStepProcessor stepProcessor;

    private final UserContextInitializer<UserContext> userContextInitializer;
    private final BotEventProducer eventProducer;

    public BotMenuUserManagerImpl(UserContextStorage userContextStorage,
                                  BotMenuStepProcessor stepProcessor,
                                  UserContextInitializer userContextInitializer, BotEventProducer eventProducer) {
        this.userContextStorage = userContextStorage;
        this.stepProcessor = stepProcessor;
        this.userContextInitializer = userContextInitializer;
        this.eventProducer = eventProducer;
    }

    @Override
    public void handleMessage(BotMessage message) {
        eventProducer.publishEvent(new BotMessageReceivedEvent(this, message));

        UserContext user = userContextStorage.getUser(message.userId());
        user = user == null ? userContextInitializer.initUser(message) : user;

        stepProcessor.processStep(message, user);
        userContextStorage.saveUser(user);
    }
}
