package link.buzalex.api;

import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

import java.util.function.BiConsumer;

public interface BotMenuStepsHolder {
    BiConsumer<BotMessage, ? super UserContext> getStep(String className, String stepName);

    void putStep(String className, String stepName, BiConsumer<BotMessage, ? super UserContext> method);
}
