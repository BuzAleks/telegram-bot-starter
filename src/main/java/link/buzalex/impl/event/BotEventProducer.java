package link.buzalex.impl.event;

import link.buzalex.models.event.BotEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BotEventProducer {
    private final ApplicationEventPublisher eventPublisher;

    public BotEventProducer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(BotEvent event) {
        eventPublisher.publishEvent(event);
    }
}
