package link.buzalex.models.event;

public class BotStepFinishedEvent extends BotEvent{
    public BotStepFinishedEvent(Object source, Long userId) {
        super(source, userId);
    }

    @Override
    public String toString() {
        return "BotStepFinishedEvent{" +
                "userId=" + userId +
                '}';
    }
}
