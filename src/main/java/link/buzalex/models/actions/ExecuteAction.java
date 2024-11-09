package link.buzalex.models.actions;

import link.buzalex.models.context.UserMessageContainer;

import java.util.Objects;
import java.util.function.Consumer;

public final class ExecuteAction extends BaseStepAction {
    private final Consumer<UserMessageContainer> executor;

    public ExecuteAction(Consumer<UserMessageContainer> executor) {
        this.executor = executor;
    }

    public Consumer<UserMessageContainer> executor() {
        return executor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ExecuteAction) obj;
        return Objects.equals(this.executor, that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executor);
    }

}
