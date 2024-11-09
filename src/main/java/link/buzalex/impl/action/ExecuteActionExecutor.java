package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.actions.ExecuteAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class ExecuteActionExecutor extends BasicActionExecutor<ExecuteAction> {
    @Override
    public Class<ExecuteAction> getActionClass() {
        return ExecuteAction.class;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, ExecuteAction action) {
        action.executor().accept(wrap(botMessage, userContext));
    }
}
