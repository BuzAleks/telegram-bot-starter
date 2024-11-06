package link.buzalex.models.message;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public record BotMessageReply(
        String text, List<List<Pair<String, String>>> keyboard
) {
    public static BotMessageReplyBuilder builder() {
        return new BotMessageReplyBuilder();
    }

}
