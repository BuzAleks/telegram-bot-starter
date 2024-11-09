package link.buzalex.impl.action;

import link.buzalex.api.UserContext;
import link.buzalex.models.actions.SaveContextDataAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class SaveContextDataActionExecutor extends BasicActionExecutor<SaveContextDataAction> {
    @Override
    public Class<SaveContextDataAction> getActionClass() {
        return SaveContextDataAction.class;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, SaveContextDataAction action) {
        userContext.getData().put(action.key(), action.value());
    }
}
