package link.buzalex.handlers;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMessageConverter;
import link.buzalex.api.ExceptionHandler;
import link.buzalex.configuration.TelegramBotProperties;
import link.buzalex.exception.BotApiServiceException;
import link.buzalex.impl.BotMenuUserManagerImpl;
import link.buzalex.models.message.BotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@ConditionalOnProperty(name = "telegram.bot.type", havingValue = "webhook")
public class TelegramWebhookHandler extends TelegramWebhookBot {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramWebhookHandler.class);

    private final BotMenuUserManagerImpl menuManager;
    private final ExceptionHandler exceptionHandler;
    private final BotMessageConverter messageMapper;
    private final TelegramBotProperties properties;

    public TelegramWebhookHandler(
            TelegramBotProperties properties,
            BotMenuUserManagerImpl menuManager,
            ExceptionHandler exceptionHandler,
            BotMessageConverter messageMapper,
            BotApiService apiService) throws TelegramApiException {
        super(properties.token);
        this.menuManager = menuManager;
        this.exceptionHandler = exceptionHandler;
        this.messageMapper = messageMapper;
        this.properties = properties;
        apiService.setExecutor(method -> {
            try {
                return execute(method);
            } catch (TelegramApiException e) {
                throw new BotApiServiceException("Exception while sending message to user", e);
            }
        });
        executeAsync(SetWebhook.builder()
                .url(properties.webhook.url + properties.webhook.path)
                .build());

        LOG.debug("TelegramWebhookHandler has successfully initialized");

    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            LOG.debug("Handled message: {}", update.toString());
            final BotMessage botMessage = messageMapper.convert(update);
            menuManager.handleMessage(botMessage);
        } catch (Exception e) {
            exceptionHandler.handleException(e, update);
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return properties.webhook.path;
    }

    @Override
    public String getBotUsername() {
        return properties.username;
    }
}
