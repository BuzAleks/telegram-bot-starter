package link.buzalex.impl;

import link.buzalex.api.*;
import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BotMenuManagerImpl implements link.buzalex.api.BotMenuManager {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuManagerImpl.class);

    public static final String START_POSITION = "BOT_MENU_START_POSITION";

    private final UserContextStorage<? super UserContext> userContextStorage;

    private final BotMenuStepProcessor stepProcessor;

    private final UserContextInitializer<? super UserContext> userContextInitializer;

    private final BotMenuStepsHolder stepsHolder;

    private final Map<String, BotMenuSectionHandler<? super UserContext>> menuHandlers;

    public BotMenuManagerImpl(UserContextStorage<? super UserContext> userContextStorage, BotMenuStepProcessor stepProcessor, UserContextInitializer<? super UserContext> userContextInitializer, BotMenuStepsHolder stepsHolder, Map<String, BotMenuSectionHandler<? super UserContext>> menuHandlers) {
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

        if (START_POSITION.equals(user.getCurrentMenuSection())) {
            Integer minOrder = null;
            for (Map.Entry<String, BotMenuSectionHandler<? super UserContext>> handler : menuHandlers.entrySet()) {
                if (handler.getValue().enterCondition(message, user)) {
                    if (minOrder == null || handler.getValue().order() < minOrder) {
                        minOrder = handler.getValue().order();
                        user.setCurrentMenuSection(handler.getKey());
                    }
                }
            }
        }
        final BotMenuSectionHandler<? super UserContext> botMenuSectionHandler = menuHandlers.get(user.getCurrentMenuSection());
        LOG.info("Chosen menu handler: " + botMenuSectionHandler.getClass().getName());
        stepProcessor.beforeStepExecution(message, user);
        final BotActions botActions = START_POSITION.equals(user.getCurrentStep()) ?
                botMenuSectionHandler.startStep(message, user) :
                stepsHolder.getStep(user.getCurrentMenuSection(), user.getCurrentStep()).apply(message, user);
        stepProcessor.afterStepExecution(message, user, botActions);
        userContextStorage.saveUser(user);
    }
}
