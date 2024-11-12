package link.buzalex.models.event;

import org.springframework.context.ApplicationEvent;

public class BotEvent extends ApplicationEvent {
    final Long userId;

    public BotEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
