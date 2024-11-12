package link.buzalex.models.event;

public class BotEntryPointSelectedEvent extends BotEvent{

    private final String entryPoint;


    public BotEntryPointSelectedEvent(Object source, Long userId, String entryPoint) {
        super(source, userId);
        this.entryPoint = entryPoint;
    }

    @Override
    public String toString() {
        return "BotEntryPointSelectedEvent{" +
                "entryPoint='" + entryPoint + '\'' +
                ", userId=" + userId +
                '}';
    }
}
