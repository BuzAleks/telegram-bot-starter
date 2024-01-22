package link.buzalex.models;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.stream.Collectors;

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
        this.keyboard = convertToSimpleKeyboard(keyboard);
        return this;
    }

//    public BotMessageReplyBuilder keyboard(List<List<Pair<String, String>>> keyboard) {
//        this.keyboard = convertToKeyboard(keyboard);
//        return this;
//    }

//    private InlineKeyboardMarkup convertToKeyboard(List<List<Pair<String, String>>> data) {
//        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        final List<List<InlineKeyboardButton>> collect = data.stream()
//                .map(list -> list.stream().map(s ->
//                                InlineKeyboardButton.builder().text(s.getFirst()).callbackData(s.getSecond()).build()
//                        ).collect(Collectors.toList())
//                ).collect(Collectors.toList());
//
//        inlineKeyboardMarkup.setKeyboard(collect);
//        return inlineKeyboardMarkup;
//    }

    private InlineKeyboardMarkup convertToSimpleKeyboard(List<List<Object>> data) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> collect = data.stream()
                .map(list -> list.stream().map(s ->
                                InlineKeyboardButton.builder().text(s.toString()).callbackData(s.toString()).build()
                        ).collect(Collectors.toList())
                ).collect(Collectors.toList());

        inlineKeyboardMarkup.setKeyboard(collect);
        return inlineKeyboardMarkup;
    }

    public BotMessageReply build() {
        return new BotMessageReply(text, keyboard);
    }
}
