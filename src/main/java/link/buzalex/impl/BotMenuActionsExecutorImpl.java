package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.menu.BaseStepActions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    private final BotApiService apiService;

    public BotMenuActionsExecutorImpl(BotApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, BaseStepActions actions) {
        final UserMessageContainer userMessageContainer = new UserMessageContainer(botMessage, userContext);
        executeReplies(userMessageContainer, actions.replies());
        executePeeks(userMessageContainer, actions.peeks());
        if (actions.clearLastMessage()) {
            apiService.clear(botMessage.messageId(), botMessage.chatId());
        }
    }

    private void executePeeks(UserMessageContainer botMessage, List<Consumer<UserMessageContainer>> peeks) {
        peeks.forEach(peek -> {
            peek.accept(botMessage);
        });
    }

    public void executeReplies(UserMessageContainer messageContainer, Map<Long, List<Function<UserMessageContainer, BotMessageReply>>> replies) {
        for (Map.Entry<Long, List<Function<UserMessageContainer, BotMessageReply>>> replyEntry : replies.entrySet()) {
            Long userId = replyEntry.getKey() == 0L ? messageContainer.message().userId() : replyEntry.getKey();
            for (Function<UserMessageContainer, BotMessageReply> botMessageReply : replyEntry.getValue()) {
                final BotMessageReply messageReply = botMessageReply.apply(messageContainer);
                apiService.sendToUser(messageReply, userId);
            }
        }
    }
}
