package link.buzalex.impl;

import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.api.BotMenuStepsHolder;
import link.buzalex.api.UserContextInitializer;
import link.buzalex.api.UserContextStorage;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BotMenuManagerImpl implements link.buzalex.api.BotMenuManager {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuManagerImpl.class);

    public static final String START_STEP = "START";

    private final UserContextStorage<? super UserContext> userContextStorage;

    private final UserContextInitializer<? super UserContext> userContextInitializer;

    private final BotMenuStepsHolder stepsHolder;

    private final Map<String, BotMenuSectionHandler<? super UserContext>> menuHandlers;

    public BotMenuManagerImpl(UserContextStorage<? super UserContext> userContextStorage, UserContextInitializer<? super UserContext> userContextInitializer, BotMenuStepsHolder stepsHolder, Map<String, BotMenuSectionHandler<? super UserContext>> menuHandlers) {
        this.userContextStorage = userContextStorage;
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
        if (user == null) {
            user = userContextInitializer.initUser(message);
        } else if (START_STEP.equals(user.getCurrentMenuSection())) {
            for (Map.Entry<String, BotMenuSectionHandler<? super UserContext>> handler : menuHandlers.entrySet()) {
                if (handler.getValue().enterCondition(message, user)) {
                    user.setCurrentMenuSection(handler.getKey());
                }
            }
        }
        final BotMenuSectionHandler<? super UserContext> botMenuSectionHandler = menuHandlers.get(user.getCurrentMenuSection());
        LOG.info("Got menu handler: " + botMenuSectionHandler.getClass().getName());
        if (START_STEP.equals(user.getCurrentStep())) {
            botMenuSectionHandler.startStep(message, user);
        } else {
            stepsHolder.getStep(user.getCurrentMenuSection(), user.getCurrentStep()).accept(message, user);
        }

        userContextStorage.saveUser(user);
    }
}
