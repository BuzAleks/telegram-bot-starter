package link.buzalex.models;

import java.time.Instant;

public record BotMessage(
        String text,
        long chatId,
        int messageId,
        long userId,
        Instant date,
        boolean edited,
        boolean fromMenu,
        boolean photo
) {
}
