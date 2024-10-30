package link.buzalex.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BotUtils {

    public static InlineKeyboardMarkup convertToSimpleInlineKeyboard(List<List<Object>> data) {
        final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> collect = data.stream()
                .map(list -> list.stream().map(s ->
                                InlineKeyboardButton.builder().text(s.toString()).callbackData(s.toString()).build()
                        ).collect(Collectors.toList())
                ).collect(Collectors.toList());

        inlineKeyboardMarkup.setKeyboard(collect);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup convertToInlineKeyboard(List<List<Pair<String, String>>> data) {
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = data.stream()
                .map(list -> list.stream()
                        .map(pair -> InlineKeyboardButton.builder()
                                .text(pair.getLeft())
                                .callbackData(pair.getRight())
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
