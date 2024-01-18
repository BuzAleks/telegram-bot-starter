package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;
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
        executeReplies(botMessage, actions.replies());
        executePeeks(botMessage, actions.peeks());
    }

    private void executePeeks(BotMessage botMessage, List<Consumer<BotMessage>> peeks) {
        peeks.forEach(peek -> {
            peek.accept(botMessage);
        });
    }

    public void executeReplies(BotMessage botMessage, Map<Long, List<Function<BotMessage, BotMessageReply>>> replies) {
        for (Map.Entry<Long, List<Function<BotMessage, BotMessageReply>>> replyEntry : replies.entrySet()) {
            Long userId = replyEntry.getKey() == 0L ? botMessage.userId() : replyEntry.getKey();
            for (Function<BotMessage, BotMessageReply> botMessageReply : replyEntry.getValue()) {
                final BotMessageReply apply = botMessageReply.apply(botMessage);
                apiService.sendToUser(apply.text(), userId);
            }
        }
    }
}
