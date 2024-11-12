package link.buzalex.models.event;

public class BotStepStartedEvent extends BotEvent{
    private final String entryPoint;
    private final String stepName;
    public BotStepStartedEvent(Object source, Long userId, String entryPoint, String stepName) {
        super(source, userId);
        this.entryPoint = entryPoint;
        this.stepName = stepName;
    }

    @Override
    public String toString() {
        return "BotStepStartedEvent{" +
                "entryPoint='" + entryPoint + '\'' +
                ", stepName='" + stepName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
