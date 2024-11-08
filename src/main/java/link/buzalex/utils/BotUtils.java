package link.buzalex.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        if (data==null) return null;
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

    public static InlineKeyboardMarkup convertStringToKeyboard(String input) {
        return convertToInlineKeyboard(convertStringToKeyboardList(input));
    }
    public static List<List<Pair<String, String>>> convertFromSimpleKeyboard(List<List<Object>> keyboard){
        return keyboard==null? null: keyboard.stream().map(s -> s.stream().map(t -> Pair.of(t.toString(), t.toString())).toList()).toList();
    }
    public static List<List<Pair<String, String>>> convertStringToKeyboardList(String input) {
        List<List<Pair<String, String>>> result = new ArrayList<>();

        // Pattern to match strings with `{}` format
        Pattern pattern = Pattern.compile("(.+?)\\[(.+?)\\]");

        // Split input by new lines
        String[] lines = input.split("\\n");

        for (String line : lines) {
            List<Pair<String, String>> innerList = new ArrayList<>();

            // Split each line by '|' delimiter
            String[] tokens = line.split("\\|");

            for (String token : tokens) {
                token = token.trim();

                Matcher matcher = pattern.matcher(token);
                if (matcher.matches()) {
                    // If token matches pattern, extract key and value
                    String key = matcher.group(1).trim();
                    String value = matcher.group(2).trim();
                    innerList.add(Pair.of(key, value));
                } else {
                    // If no `{}`, use the token itself as key and value
                    innerList.add(Pair.of(token, token));
                }
            }

            result.add(innerList);
        }

        return result;
    }
}
