package link.buzalex.models.action;

import link.buzalex.models.UserMessageContainer;

import java.util.function.Consumer;

public class ExecuteAction extends BaseStepAction {
    private final Consumer<UserMessageContainer> executor;

    public ExecuteAction(Consumer<UserMessageContainer> executor) {
        this.executor = executor;
    }

    public Consumer<UserMessageContainer> getExecutor() {
        return executor;
    }
}
