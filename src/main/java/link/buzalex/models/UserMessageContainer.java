package link.buzalex.models;

import link.buzalex.api.UserContext;

public record UserMessageContainer(BotMessage message, UserContext context) {
}
