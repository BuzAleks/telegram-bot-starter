package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.action.ExecuteAction;
import link.buzalex.models.action.RemoveMessageAction;
import link.buzalex.models.action.SendMessageAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

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
            if (action instanceof ExecuteAction executeAction) executeAction.getExecutor().accept(userMessageContainer);
            if (action instanceof RemoveMessageAction) apiService.clear(botMessage.messageId(), botMessage.chatId());
            if (action instanceof SendMessageAction messageAction)
                executeReplies(userMessageContainer, messageAction.getUserId(), messageAction.getMessageFunction());
        }
    }

    public void executeReplies(UserMessageContainer messageContainer, Long id, Function<UserMessageContainer, BotMessageReply> messageFunction) {
        Long userId = id == 0L ? messageContainer.message().userId() : id;
        final BotMessageReply messageReply = messageFunction.apply(messageContainer);
        apiService.sendToUser(messageReply, userId);
    }
}
