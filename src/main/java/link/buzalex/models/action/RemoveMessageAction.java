package link.buzalex.models.action;

public class RemoveMessageAction extends BaseStepAction{
    private final boolean removeLastMessage;

    public RemoveMessageAction(boolean removeLastMessage) {
        this.removeLastMessage = removeLastMessage;
    }

    public boolean isRemoveLastMessage() {
        return removeLastMessage;
    }
}
