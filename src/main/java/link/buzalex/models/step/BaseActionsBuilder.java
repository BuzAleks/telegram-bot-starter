package link.buzalex.models.step;

import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.action.ExecuteAction;
import link.buzalex.models.action.RemoveMessageAction;
import link.buzalex.models.action.SendMessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseActionsBuilder<T extends BaseActionsBuilder<T>> {
    List<BaseStepAction> actions = new ArrayList<>();

    BaseActionsBuilder() {
    }

    @SuppressWarnings("unchecked")
    public T removeLastMessage() {
        actions.add(new RemoveMessageAction(true));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T message(Long user, Function<UserMessageContainer, BotMessageReply> message) {
        actions.add(new SendMessageAction(user, message));
        return (T) this;
    }

    public T message(Function<UserMessageContainer, BotMessageReply> message) {
        return this.message(0L, message);
    }

    public T message(BotMessageReply message) {
        return this.message(0L, mes -> message);

    }

    public T message(String message) {
        return this.message(new BotMessageReply(message, null));
    }

    @SuppressWarnings("unchecked")
    public T execute(Consumer<UserMessageContainer> executor) {
        actions.add(new ExecuteAction(executor));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public ConditionalActionsBuilder<T> ifTrue(Predicate<UserMessageContainer> condition) {
        return new ConditionalActionsBuilder<>((T) this, condition);
    }
}
