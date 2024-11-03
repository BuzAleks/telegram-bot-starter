package link.buzalex.models.action;

import link.buzalex.models.message.BotMessageReply;
import link.buzalex.models.context.UserMessageContainer;

import java.util.function.Function;

public record SendMessageAction(Long userId,
                                Function<UserMessageContainer, BotMessageReply> messageFunction) implements BaseStepAction {
}
