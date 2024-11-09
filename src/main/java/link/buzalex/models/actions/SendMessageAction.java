package link.buzalex.models.actions;

import link.buzalex.models.message.BotMessageReply;
import link.buzalex.models.context.UserMessageContainer;

import java.util.Objects;
import java.util.function.Function;

public final class SendMessageAction extends BaseStepAction {
    private final Long userId;
    private final Function<UserMessageContainer, BotMessageReply> messageFunction;

    public SendMessageAction(Long userId,
                             Function<UserMessageContainer, BotMessageReply> messageFunction) {
        this.userId = userId;
        this.messageFunction = messageFunction;
    }

    public Long userId() {
        return userId;
    }

    public Function<UserMessageContainer, BotMessageReply> messageFunction() {
        return messageFunction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SendMessageAction) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.messageFunction, that.messageFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, messageFunction);
    }

}
