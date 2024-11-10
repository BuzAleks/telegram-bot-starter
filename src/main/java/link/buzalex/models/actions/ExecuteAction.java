package link.buzalex.models.actions;

import link.buzalex.models.context.UserMessageContainer;

import java.util.Objects;
import java.util.function.Consumer;

public record ExecuteAction(Consumer<UserMessageContainer> executor) implements Action {
}
