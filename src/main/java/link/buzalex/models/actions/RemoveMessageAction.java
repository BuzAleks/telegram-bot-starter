package link.buzalex.models.actions;

import java.util.Objects;

public final class RemoveMessageAction extends BaseStepAction {
    private final boolean removeLastMessage;

    public RemoveMessageAction(boolean removeLastMessage) {
        this.removeLastMessage = removeLastMessage;
    }

    public boolean removeLastMessage() {
        return removeLastMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RemoveMessageAction) obj;
        return this.removeLastMessage == that.removeLastMessage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(removeLastMessage);
    }
}
