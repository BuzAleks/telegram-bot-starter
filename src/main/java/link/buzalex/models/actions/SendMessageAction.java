package link.buzalex.models.actions;

import link.buzalex.models.context.UserMessageContainer;
import link.buzalex.models.message.BotMessageReply;

import java.util.function.Function;

public record SendMessageAction(Long userId,
                                Function<UserMessageContainer, BotMessageReply> messageFunction) implements Action {

}
