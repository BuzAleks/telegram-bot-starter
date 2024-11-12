package link.buzalex.impl.event;

import link.buzalex.models.event.BotEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BotEventLogListenerImpl {
    private static final Logger LOG = LoggerFactory.getLogger(BotEventLogListenerImpl.class);

    @EventListener
    public void handleBotEvent(BotEvent event) {
        LOG.debug(event.toString());
    }
}
