package link.buzalex.models.action;

import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserMessageContainer;

import java.util.function.Function;

public class SendMessageAction extends BaseStepAction {
    private final Long userId;
    private final Function<UserMessageContainer, BotMessageReply> messageFunction;

    public SendMessageAction(Long userId, Function<UserMessageContainer, BotMessageReply> messageFunction) {
        this.userId = userId;
        this.messageFunction = messageFunction;
    }

    public Long getUserId() {
        return userId;
    }

    public Function<UserMessageContainer, BotMessageReply> getMessageFunction() {
        return messageFunction;
    }
}
