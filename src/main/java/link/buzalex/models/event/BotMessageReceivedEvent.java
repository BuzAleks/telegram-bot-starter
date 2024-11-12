package link.buzalex.models.event;

import link.buzalex.models.message.BotMessage;

public class BotMessageReceivedEvent extends BotEvent{
    private final BotMessage message;
    public BotMessageReceivedEvent(Object source, BotMessage message) {
        super(source, message.userId());
        this.message = message;
    }

    @Override
    public String toString() {
        return "BotMessageReceivedEvent{" +
                "message=" + message.text() +
                ", userId=" + userId +
                '}';
    }
}
