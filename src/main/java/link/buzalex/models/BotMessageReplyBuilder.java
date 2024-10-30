package link.buzalex.models;

import link.buzalex.utils.BotUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public final class BotMessageReplyBuilder {
    private String text;
    private InlineKeyboardMarkup keyboard;

    BotMessageReplyBuilder() {
    }

    public BotMessageReplyBuilder text(String text) {
        this.text = text;
        return this;
    }

    public BotMessageReplyBuilder simpleKeyboard(List<List<Object>> keyboard) {
        this.keyboard = BotUtils.convertToSimpleInlineKeyboard(keyboard);
        return this;
    }

    public BotMessageReplyBuilder keyboard(List<List<Pair<String, String>>> keyboard) {
        this.keyboard = BotUtils.convertToInlineKeyboard(keyboard);
        return this;
    }

    public BotMessageReply build() {
        return new BotMessageReply(text, keyboard);
    }
}
