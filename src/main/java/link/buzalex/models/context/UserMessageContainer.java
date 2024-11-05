package link.buzalex.models.context;

import link.buzalex.models.message.BotMessage;

public record UserMessageContainer(BotMessage message, UserContextWrapper context) {
}
