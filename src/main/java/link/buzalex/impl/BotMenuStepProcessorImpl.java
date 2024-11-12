package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.UserContext;
import link.buzalex.impl.event.BotEventProducer;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.action.ActionStackItem;
import link.buzalex.models.action.ActionsContainer;
import link.buzalex.models.event.BotEntryPointFinishedEvent;
import link.buzalex.models.event.BotEntryPointSelectedEvent;
import link.buzalex.models.event.BotStepFinishedEvent;
import link.buzalex.models.event.BotStepStartedEvent;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.step.BotStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Deque;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepProcessorImpl.class);
    private final BotEventProducer eventProducer;
    private final BotMenuActionsExecutor actionsExecutor;
    private final BotItemsHolder stepsHolder;

    public BotMenuStepProcessorImpl(BotEventProducer eventProducer, BotMenuActionsExecutor actionsExecutor, BotItemsHolder stepsHolder) {
        this.eventProducer = eventProducer;
        this.actionsExecutor = actionsExecutor;
        this.stepsHolder = stepsHolder;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {
        String menuSection = determineMenuSection(message, user);
        if (menuSection == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }
        Deque<ActionStackItem> stack = user.getStack();

        ActionCursor cursor;

        if (stack.isEmpty()) {
            String rootStepName = stepsHolder.getEntryPoint(menuSection).rootStepName();
            BotStep step = stepsHolder.getStep(rootStepName);
            ActionsContainer action = step.stepActions();
            cursor = new ActionCursor(step, action, null);
            user.getStepsHistory().push(rootStepName);
        } else {
            cursor = ActionCursor.fromStack(stack.pop(), stepsHolder);
        }
        BotStep step = cursor.step();

        if (user.getStepsHistory().isEmpty() || user.getStepsHistory().peek().equals("finish")){
            eventProducer.publishEvent(new BotStepStartedEvent(this,user.getId(),user.getEntryPoint(),step.name()));
        }

        while (cursor != null) {
            cursor = actionsExecutor.executeAndMoveCursor(message, user, cursor);
            if (cursor != null && !cursor.step().equals(step)){
                eventProducer.publishEvent(new BotStepFinishedEvent(this,user.getId()));
                step = cursor.step();
            }
        }
        if (user.getEntryPoint() == null){
            eventProducer.publishEvent(new BotStepFinishedEvent(this,user.getId()));
            eventProducer.publishEvent(new BotEntryPointFinishedEvent(this,user.getId()));
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
            user.setEntryPoint(botEntryPoint.name());
            eventProducer.publishEvent(new BotEntryPointSelectedEvent(this, user.getId(), user.getEntryPoint()));
        }
        return user.getEntryPoint();
    }
}
