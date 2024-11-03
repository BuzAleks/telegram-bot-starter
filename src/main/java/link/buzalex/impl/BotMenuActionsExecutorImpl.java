package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.action.ExecuteAction;
import link.buzalex.models.action.RemoveMessageAction;
import link.buzalex.models.action.SendMessageAction;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.message.BotMessageReply;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    private final BotApiService apiService;

    public BotMenuActionsExecutorImpl(BotApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, List<BaseStepAction> actions) {
        final UserMessageContainer userMessageContainer = new UserMessageContainer(botMessage, userContext);
        for (BaseStepAction action : actions) {
            if (action instanceof ExecuteAction executeAction)
                executeAction(userMessageContainer, executeAction);
            else if (action instanceof SendMessageAction executeAction)
                executeAction(userMessageContainer, executeAction);
            else if (action instanceof RemoveMessageAction executeAction)
                executeAction(userMessageContainer, executeAction);
        }
    }

    private void executeAction(UserMessageContainer userMessageContainer, ExecuteAction action) {
        action.executor().accept(userMessageContainer);
    }

    private void executeAction(UserMessageContainer userMessageContainer, SendMessageAction action) {
        Long userId = action.userId() == 0L ? userMessageContainer.message().userId() : action.userId();
        final BotMessageReply messageReply = action.messageFunction().apply(userMessageContainer);
        apiService.sendToUser(messageReply, userId);
    }

    private void executeAction(UserMessageContainer userMessageContainer, RemoveMessageAction action) {
        apiService.clear(userMessageContainer.message().messageId(), userMessageContainer.message().chatId());
    }
}
