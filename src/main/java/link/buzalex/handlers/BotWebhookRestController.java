package link.buzalex.handlers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@ConditionalOnBean(TelegramWebhookHandler.class)
public class BotWebhookRestController {
    private final TelegramWebhookHandler telegramWebHookHandler;

    public BotWebhookRestController(TelegramWebhookHandler telegramWebHookHandler) {
        this.telegramWebHookHandler = telegramWebHookHandler;
    }

    @PostMapping("${telegram.bot.webhook.path}")
    public void webhookHandle(@RequestBody Update update) {
        telegramWebHookHandler.onWebhookUpdateReceived(update);
    }
}
