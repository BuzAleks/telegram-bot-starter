package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.UserContext;
import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.action.ExecuteAction;
import link.buzalex.models.action.RemoveMessageAction;
import link.buzalex.models.action.SendMessageAction;
import link.buzalex.models.context.UserContextWrapper;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.message.BotMessageReply;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BotMenuActionsExecutorImpl implements BotMenuActionsExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuActionsExecutorImpl.class);

    private final BotApiService apiService;

    private final BotTextExpressionEvaluator expressionEvaluator;

    public BotMenuActionsExecutorImpl(BotApiService apiService, BotTextExpressionEvaluator expressionEvaluator) {
        this.apiService = apiService;
        this.expressionEvaluator = expressionEvaluator;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, BaseStepAction action) {
        final UserMessageContainer userMessageContainer = getUserMessageContainer(botMessage, userContext);
        if (action instanceof ExecuteAction executeAction)
            executeAction(userMessageContainer, executeAction);
        else if (action instanceof SendMessageAction executeAction) {
            executeAction(botMessage, userContext , executeAction);
        } else if (action instanceof RemoveMessageAction executeAction)
            executeAction(userMessageContainer, executeAction);
    }

    @NotNull
    private static UserMessageContainer getUserMessageContainer(BotMessage botMessage, UserContext userContext) {
        UserContextWrapper userContextWrapper = new UserContextWrapper(userContext);
        return new UserMessageContainer(botMessage, userContextWrapper);
    }

    private void executeAction(BotMessage botMessage, UserContext userContext, SendMessageAction action) {
        Long userId = action.userId() == 0L ? botMessage.userId() : action.userId();
        final BotMessageReply messageReply = action.messageFunction().apply(getUserMessageContainer(botMessage, userContext));
        String formattedText = expressionEvaluator.evaluate(messageReply.text(), userContext.getData());
        List<List<Pair<String, String>>> formattedKeyboard = Optional.ofNullable(messageReply.keyboard())
                .map(keyboard -> keyboard.stream()
                        .map(pairs -> pairs.stream()
                                .map(formatStringPair(userContext.getData()))
                                .collect(Collectors.toList())
                        )
                        .collect(Collectors.toList())
                )
                .orElse(null);
        apiService.sendToUser(new BotMessageReply(formattedText, formattedKeyboard), userId);
    }

    private void executeAction(UserMessageContainer userMessageContainer, ExecuteAction action) {
        action.executor().accept(userMessageContainer);
    }

    @NotNull
    private Function<Pair<String, String>, Pair<String, String>> formatStringPair(Map<String, Object> context) {
        return pair -> Pair.of(
                expressionEvaluator.evaluate(pair.getLeft(), context),
                expressionEvaluator.evaluate(pair.getRight(), context)
        );
    }

    private void executeAction(UserMessageContainer userMessageContainer, RemoveMessageAction action) {
        apiService.clear(userMessageContainer.message().messageId(), userMessageContainer.message().chatId());
    }
}
