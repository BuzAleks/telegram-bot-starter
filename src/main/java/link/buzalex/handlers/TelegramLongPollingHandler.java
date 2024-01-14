package link.buzalex.handlers;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMessageConverter;
import link.buzalex.api.ExceptionHandler;
import link.buzalex.configuration.TelegramBotProperties;
import link.buzalex.exception.BotApiServiceException;
import link.buzalex.impl.BotMenuManagerImpl;
import link.buzalex.models.BotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@ConditionalOnProperty(name = "telegram.bot.type", havingValue = "longPolling", matchIfMissing = true)
public class TelegramLongPollingHandler extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramLongPollingHandler.class);

    static DefaultBotOptions options = new DefaultBotOptions();

    static {
        options.setMaxThreads(4);
    }

    private final BotMenuManagerImpl menuManager;
    private final ExceptionHandler exceptionHandler;
    private final BotMessageConverter messageMapper;
    private final TelegramBotProperties properties;

    public TelegramLongPollingHandler(
            TelegramBotProperties properties,
            BotMenuManagerImpl menuManager,
            ExceptionHandler exceptionHandler,
            BotMessageConverter messageMapper,
            BotApiService apiService) {

        super(options, properties.token);
        this.menuManager = menuManager;
        this.exceptionHandler = exceptionHandler;
        this.messageMapper = messageMapper;
        this.properties = properties;
        apiService.setExecutor(method -> {
            try {
                executeAsync(method);
            } catch (TelegramApiException e) {
                throw new BotApiServiceException("Exception while sending message to user", e);
            }
        });
    }

    @Override
    public String getBotUsername() {
        return properties.username;
    }


    @Override
    public String getBotToken() {
        return properties.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            LOG.info("Handled message: " + update.toString());
            final BotMessage botMessage = messageMapper.convert(update);
            menuManager.handleMessage(botMessage);
        } catch (Exception e) {
            exceptionHandler.handleException(e, update);
        }
    }

    @Override
    public void onRegister() {
        LOG.info("TelegramLongPollingHandler has successfully registered");
    }
}
