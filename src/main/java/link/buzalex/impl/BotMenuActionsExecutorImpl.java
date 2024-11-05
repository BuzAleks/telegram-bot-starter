package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.*;
import link.buzalex.models.context.UserContextWrapper;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.message.BotMessageReply;
import link.buzalex.models.step.BotStepsChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuActionsExecutorImpl.class);

    private final BotApiService apiService;

    public BotMenuActionsExecutorImpl(BotApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, BaseStepAction action) {
        UserContextWrapper userContextWrapper = new UserContextWrapper(userContext);
        final UserMessageContainer userMessageContainer = new UserMessageContainer(botMessage, userContextWrapper);
        if (action instanceof ExecuteAction executeAction)
            executeAction(userMessageContainer, executeAction);
        else if (action instanceof SendMessageAction executeAction)
            executeAction(userMessageContainer, executeAction);
        else if (action instanceof RemoveMessageAction executeAction)
            executeAction(userMessageContainer, executeAction);
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
