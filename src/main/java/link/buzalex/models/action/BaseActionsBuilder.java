package link.buzalex.models.action;

import link.buzalex.models.actions.*;
import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessageReply;
import link.buzalex.utils.BotUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseActionsBuilder<T extends BaseActionsBuilder<T>> {
    final ActionsContainer stepActions;
    final BotStepBuilder stepBuilder;
    final Set<String> actionNames = new HashSet<>();

    BaseActionsBuilder(ActionsContainer stepActions, BotStepBuilder stepBuilder) {
        this.stepActions = stepActions;
        this.stepBuilder = stepBuilder;
        ActionsContainer container = stepActions;
        while (container!=null){
            actionNames.add(container.getName());
            container = container.getNextAction();
        }
    }

    @SuppressWarnings("unchecked")
    public T removeLastMessage() {
        putAction(new RemoveMessageAction(true));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T message(Long user, Function<UserMessageContainer, BotMessageReply> message) {
        putAction(new SendMessageAction(user, message));
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

    public T keyboard(String message, List<List<Object>> keyboard) {
        return this.message(BotMessageReply.builder().text(message).simpleKeyboard(keyboard).build());
    }

    public T keyboard(String message, Function<UserMessageContainer, List<List<Object>>> keyboardFunc) {
        return this.message(c -> BotMessageReply.builder().text(message).simpleKeyboard(keyboardFunc.apply(c)).build());
    }

    @SuppressWarnings("unchecked")
    public T execute(Consumer<UserMessageContainer> executor) {
        putAction(new ExecuteAction(executor));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T useStep(String stepName) {
        putAction(new ReuseStepsChainAction(stepName));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T putContextData(String key, Object data) {
        putAction(new ExecuteAction(s -> s.context().put(key, data)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextData(String key, Function<Object, Object> func) {
        putAction(new ExecuteAction(s -> s.context().modifyIfPresents(key, func)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextDataAsString(String key, Function<String, Object> func) {
        putAction(new ExecuteAction(s -> s.context().modifyIfPresentsAsString(key, func)));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T modifyContextDataAsInt(String key, Function<Integer, Object> func) {
        putAction(new ExecuteAction(s -> s.context().modifyIfPresentsAsInt(key, func)));
        return (T) this;
    }

    void putAction(Action action) {
        String methodName = StackWalker.getInstance()
                .walk(frames -> frames.skip(1)
                        .findFirst()
                        .map(StackWalker.StackFrame::getMethodName)
                        .orElse("unknown"));
        if (stepActions.getAction() == null) {
            stepActions.setName(methodName);
            stepActions.setAction(action);
            actionNames.add(methodName);
        } else {
            ActionsContainer container = stepActions;
            while (container.getNextAction() != null) {
                if (actionNames.contains(methodName)) {
                    int counter = 1;
                    for (int i = 0; i < counter; i++) {
                        String newName = methodName + "#" + counter;
                        if (!actionNames.contains(newName)) {
                            methodName = newName;
                            break;
                        }
                    }
                }
                container = container.getNextAction();
            }
            container.setNextAction(new ActionsContainer(methodName, action));
            actionNames.add(methodName);
        }
    }
}
