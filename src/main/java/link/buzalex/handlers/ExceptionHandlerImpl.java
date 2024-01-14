package link.buzalex.handlers;

import link.buzalex.api.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@ConditionalOnMissingBean(value = ExceptionHandler.class, ignored = ExceptionHandlerImpl.class)
public class ExceptionHandlerImpl implements ExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerImpl.class);

    @Override
    public void handleException(Exception ex, Update update) {
        LOG.error(ex.getMessage(), ex);
        throw new RuntimeException(ex);
    }
}
