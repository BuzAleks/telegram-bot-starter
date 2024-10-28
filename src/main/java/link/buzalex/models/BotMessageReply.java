package link.buzalex.models;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public record BotMessageReply(
        String text, InlineKeyboardMarkup keyboard
) {
    public static BotMessageReplyBuilder builder() {
        return new BotMessageReplyBuilder();
    }

}
