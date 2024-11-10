package link.buzalex.impl;

import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.impl.action.ActionExecutor;
import link.buzalex.models.action.ActionStackObject;
import link.buzalex.models.actions.Action;
import link.buzalex.models.message.BotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuActionsExecutorImpl.class);

    final Map<Class<?>, ActionExecutor<?>> actionExecutors;

    public BotMenuActionsExecutorImpl(List<ActionExecutor<?>> actionExecutors) {
        this.actionExecutors = actionExecutors.stream().collect(Collectors.toMap(ActionExecutor::getActionClass, s -> s));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActionStackObject executeAndMoveCursor(BotMessage botMessage, UserContext userContext, ActionStackObject cursor) {
        Action action = cursor.subAction() == null ? cursor.action().getAction() : cursor.subAction().getAction();
        ActionExecutor actionExecutor = actionExecutors.get(action.getClass());
        ActionStackObject actionStackObject = actionExecutor.executeAndMoveCursor(cursor, botMessage, userContext, action);
        return actionStackObject;
    }
}
