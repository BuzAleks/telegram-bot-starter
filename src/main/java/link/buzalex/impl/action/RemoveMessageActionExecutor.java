package link.buzalex.impl.action;

import link.buzalex.api.BotApiService;
import link.buzalex.api.UserContext;
import link.buzalex.models.actions.RemoveMessageAction;
import link.buzalex.models.message.BotMessage;
import org.springframework.stereotype.Component;

@Component
public class RemoveMessageActionExecutor extends BasicActionExecutor<RemoveMessageAction> {
    private final BotApiService apiService;

    public RemoveMessageActionExecutor(BotApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Class<RemoveMessageAction> getActionClass() {
        return RemoveMessageAction.class;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, RemoveMessageAction action) {
        userContext.getData().remove("lasKeyboardId");
        apiService.clear(botMessage.messageId(), botMessage.chatId());
    }
}
