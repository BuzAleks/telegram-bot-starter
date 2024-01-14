package link.buzalex.models;

import java.time.Instant;

public final class BotMessageBuilder {
    private String text;
    private long chatId;
    private int messageId;
    private long userId;
    private Instant date;
    private boolean edited;
    private boolean fromMenu;
    private boolean photo;

    private BotMessageBuilder() {
    }

    public static BotMessageBuilder builder() {
        return new BotMessageBuilder();
    }

    public BotMessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public BotMessageBuilder chatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public BotMessageBuilder messageId(int messageId) {
        this.messageId = messageId;
        return this;
    }

    public BotMessageBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    public BotMessageBuilder date(Instant date) {
        this.date = date;
        return this;
    }

    public BotMessageBuilder edited(boolean edited) {
        this.edited = edited;
        return this;
    }

    public BotMessageBuilder fromMenu(boolean fromMenu) {
        this.fromMenu = fromMenu;
        return this;
    }

    public BotMessageBuilder photo(boolean photo) {
        this.photo = photo;
        return this;
    }

    public BotMessage build() {
        return new BotMessage(text, chatId, messageId, userId, date, edited, fromMenu, photo);
    }
}
