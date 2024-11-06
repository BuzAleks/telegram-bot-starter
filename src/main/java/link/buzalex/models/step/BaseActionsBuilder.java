package link.buzalex.models.step;

import link.buzalex.models.action.*;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessageReply;
import link.buzalex.utils.BotUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseActionsBuilder<T extends BaseActionsBuilder<T>> {
    List<BaseStepAction> actions = new ArrayList<>();
    final BotStepBuilder stepBuilder;

    BaseActionsBuilder(BotStepBuilder stepBuilder) {
        this.stepBuilder = stepBuilder;
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
        return this.message(0L, userMessageContainer -> message);

    }

    public T message(String message) {
        return this.message(new BotMessageReply(message, null));
    }

    public T fKeyboard(String message, String keyboard) {
        return this.message(new BotMessageReply(message, BotUtils.convertStringToKeyboardList(keyboard)));
    }


    @SuppressWarnings("unchecked")
    public T execute(Consumer<UserMessageContainer> executor) {
        actions.add(new ExecuteAction(executor));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T useStep(String stepName) {
        actions.add(new ReuseStepsChainAction(stepName));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public ConditionalActionsBuilder<T> ifTrue(Predicate<UserMessageContainer> condition) {
        return new ConditionalActionsBuilder<>((T) this, condition);
    }

    @SuppressWarnings("unchecked")
    public T putContextData(String key, Object data) {
        actions.add(new ExecuteAction(s -> s.context().put(key, data)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextData(String key, Function<Object, Object> func) {
        actions.add(new ExecuteAction(s -> s.context().modifyIfPresents(key, func)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextDataAsString(String key, Function<String, Object> func) {
        actions.add(new ExecuteAction(s -> s.context().modifyIfPresentsAsString(key, func)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextDataAsInt(String key, Function<Integer, Object> func) {
        actions.add(new ExecuteAction(s -> s.context().modifyIfPresentsAsInt(key, func)));
        return (T) this;
    }
}
