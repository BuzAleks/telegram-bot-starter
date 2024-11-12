package link.buzalex.impl;

import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.impl.action.ActionExecutor;
import link.buzalex.impl.event.BotEventProducer;
import link.buzalex.models.action.ActionCursor;
import link.buzalex.models.actions.Action;
import link.buzalex.models.event.BotActionFinishedEvent;
import link.buzalex.models.event.BotActionStartedEvent;
import link.buzalex.models.message.BotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    final Map<Class<?>, ActionExecutor<?>> actionExecutors;
    private final BotEventProducer eventProducer;

    public BotMenuActionsExecutorImpl(List<ActionExecutor<?>> actionExecutors, BotEventProducer eventProducer) {
        this.actionExecutors = actionExecutors.stream().collect(Collectors.toMap(ActionExecutor::getActionClass, s -> s));
        this.eventProducer = eventProducer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionCursor executeAndMoveCursor(BotMessage botMessage, UserContext userContext, ActionCursor cursor) {
        Action action = cursor.subAction() == null ? cursor.action().getAction() : cursor.subAction().getAction();
        ActionExecutor actionExecutor = actionExecutors.get(action.getClass());
        eventProducer.publishEvent(BotActionStartedEvent.build(actionExecutor, userContext, cursor));
        ActionCursor actionCursor = actionExecutor.executeAndMoveCursor(cursor, botMessage, userContext, action);
        eventProducer.publishEvent(BotActionFinishedEvent.build(actionExecutor, userContext, cursor));
        return actionCursor;
    }
}
