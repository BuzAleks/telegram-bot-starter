package link.buzalex.models.action;

import link.buzalex.models.context.UserMessageContainer;

import java.util.function.Consumer;

public record ExecuteAction(Consumer<UserMessageContainer> executor) implements BaseStepAction {
}
