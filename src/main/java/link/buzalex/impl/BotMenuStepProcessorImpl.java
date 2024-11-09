package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.BaseStepAction;
import link.buzalex.models.actions.ConditionalAction;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import link.buzalex.utils.ActionStackUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepProcessorImpl.class);

    private final BotMenuActionsExecutor actionsExecutor;
    private final BotItemsHolder stepsHolder;

    public BotMenuStepProcessorImpl(BotMenuActionsExecutor actionsExecutor, BotItemsHolder stepsHolder) {
        this.actionsExecutor = actionsExecutor;
        this.stepsHolder = stepsHolder;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {
        LOG.debug("Message processing is being started. Menu section: {}, steps: {}", user.getEntryPoint(), user.getStack());
        String menuSection = determineMenuSection(message, user);
        if (menuSection == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }
        Deque<ActionStackItem> stack = user.getStack();

        ActionStackObject cursor;

        if (stack.isEmpty()) {
            String rootStepName = stepsHolder.getEntryPoint(menuSection).rootStepName();
            BotStep step = stepsHolder.getStep(rootStepName);
            BaseStepAction action = step.stepActions().getFirst();
            cursor = new ActionStackObject(step, action, null);
            LOG.debug("First step -> {}, {}", rootStepName, step);
        } else {
            cursor = ActionStackUtils.convert(stack.pop(), stepsHolder);
        }

        while (cursor != null) {
            LOG.debug("Cursor-> {}", cursor);
            cursor = actionsExecutor.executeAndMoveCursor(message, user, cursor);
        }
    }

    private String determineMenuSection(BotMessage message, UserContext user) {
        BotEntryPoint botEntryPoint = null;
        if (user.getEntryPoint() == null) {
            Integer minOrder = null;
            for (BotEntryPoint botAction : stepsHolder.getEntryPoints().values()) {
                if (botAction.selector().test(message)) {
                    if (minOrder == null || botAction.order() < minOrder) {
                        minOrder = botAction.order();
                        botEntryPoint = botAction;
                    }
                }
            }
        }
        if (botEntryPoint != null) {
            LOG.debug("Chosen menu section: {}", botEntryPoint.name());
            user.setEntryPoint(botEntryPoint.name());
        }
        return user.getEntryPoint();
    }
}
