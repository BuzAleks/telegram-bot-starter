package link.buzalex.models.event;

public class BotEntryPointFinishedEvent extends BotEvent{
    public BotEntryPointFinishedEvent(Object source, Long userId) {
        super(source, userId);
    }

    @Override
    public String toString() {
        return "BotEntryPointFinishedEvent{" +
                "userId=" + userId +
                '}';
    }
}
