package link.buzalex.impl;

import link.buzalex.api.*;
import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BotMenuManagerImpl implements link.buzalex.api.BotMenuManager {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuManagerImpl.class);

    private final UserContextStorage<UserContext> userContextStorage;

    private final BotMenuStepProcessor stepProcessor;

    private final UserContextInitializer<UserContext> userContextInitializer;

    private final BotMenuStepsHolder stepsHolder;

    private final Map<String, BotMenuSectionHandler> menuHandlers;

    public BotMenuManagerImpl(UserContextStorage userContextStorage,
                              BotMenuStepProcessor stepProcessor,
                              UserContextInitializer userContextInitializer,
                              BotMenuStepsHolder stepsHolder,
                              Map<String, BotMenuSectionHandler> menuHandlers) {
        this.userContextStorage = userContextStorage;
        this.stepProcessor = stepProcessor;
        this.userContextInitializer = userContextInitializer;
        this.stepsHolder = stepsHolder;
        this.menuHandlers = menuHandlers;
        menuHandlers.forEach((key, value) ->
                LOG.info("Got menuHandler: " + key)
        );
    }

    @Override
    public void handleMessage(BotMessage message) {
        LOG.info("Got message: " + message.text());

        UserContext user = userContextStorage.getUser(message.userId());
        user = user == null ? userContextInitializer.initUser(message) : user;

        if (user.getCurrentMenuSection() == null) {
            Integer minOrder = null;
            for (Map.Entry<String, BotMenuSectionHandler> handler : menuHandlers.entrySet()) {
                if (handler.getValue().enterCondition(message, user)) {
                    if (minOrder == null || handler.getValue().order() < minOrder) {
                        minOrder = handler.getValue().order();
                        user.setCurrentMenuSection(handler.getKey());
                    }
                }
            }
        }
        if (user.getCurrentMenuSection() == null) {
            LOG.warn("Step cannot be determined");
            return;
        }
        final BotMenuSectionHandler<? super UserContext> botMenuSectionHandler = menuHandlers.get(user.getCurrentMenuSection());
        LOG.info("Chosen menu handler: " + botMenuSectionHandler.getClass().getName());
        stepProcessor.beforeStepExecution(message, user);
        if (user.getCurrentMenuSection() == null) {
            LOG.warn("Step finished");
            return;
        }
        final BotActions botActions = user.getStepFunc() == null ?
                botMenuSectionHandler.startStep(message, user) :
                user.getStepFunc().apply(message, user);
        stepProcessor.afterStepExecution(message, user, botActions);
        userContextStorage.saveUser(user);
    }
}
