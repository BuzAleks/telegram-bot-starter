package link.buzalex.models.message;

import link.buzalex.utils.BotUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public final class BotMessageReplyBuilder {
    private String text;
    private List<List<Pair<String, String>>> keyboard;

    BotMessageReplyBuilder() {
    }

    public BotMessageReplyBuilder text(String text) {
        this.text = text;
        return this;
    }

    public BotMessageReplyBuilder simpleKeyboard(List<List<Object>> keyboard) {
        this.keyboard = BotUtils.convertFromSimpleKeyboard(keyboard);
        return this;
    }

    public BotMessageReplyBuilder keyboard(List<List<Pair<String, String>>> keyboard) {
        this.keyboard = keyboard;
        return this;
    }

    public BotMessageReply build() {
        return new BotMessageReply(text, keyboard);
    }
}
