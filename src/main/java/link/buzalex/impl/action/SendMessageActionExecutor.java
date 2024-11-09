package link.buzalex.impl.action;

import link.buzalex.api.BotApiService;
import link.buzalex.api.UserContext;
import link.buzalex.impl.BotTextExpressionEvaluator;
import link.buzalex.models.actions.SendMessageAction;
import link.buzalex.models.message.BotMessage;
import link.buzalex.models.message.BotMessageReply;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SendMessageActionExecutor extends BasicActionExecutor<SendMessageAction> {
    private final BotApiService apiService;

    private final BotTextExpressionEvaluator expressionEvaluator;

    public SendMessageActionExecutor(BotApiService apiService, BotTextExpressionEvaluator expressionEvaluator) {
        this.apiService = apiService;
        this.expressionEvaluator = expressionEvaluator;
    }

    @Override
    public Class<SendMessageAction> getActionClass() {
        return SendMessageAction.class;
    }

    @Override
    public void execute(BotMessage botMessage, UserContext userContext, SendMessageAction action) {
        Long userId = action.userId() == 0L ? botMessage.userId() : action.userId();
        final BotMessageReply messageReply = action.messageFunction().apply(wrap(botMessage, userContext));
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

    private Function<Pair<String, String>, Pair<String, String>> formatStringPair(Map<String, Object> context) {
        return pair -> Pair.of(
                expressionEvaluator.evaluate(pair.getLeft(), context),
                expressionEvaluator.evaluate(pair.getRight(), context)
        );
    }
}
