package link.buzalex.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class BotWebhookRestController {
    private static final Logger LOG = LoggerFactory.getLogger(BotWebhookRestController.class);

    @Autowired
    private TelegramWebhookHandler telegramWebHookHandler;

    @PostMapping("${telegram.bot.webhook.path}")
    public void webhookHandle(@RequestBody Update update) {
        telegramWebHookHandler.onWebhookUpdateReceived(update);
    }
}
