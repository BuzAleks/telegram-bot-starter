package link.buzalex.api;

import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface BotMenuStepsHolder {
    BiFunction<BotMessage, ? super UserContext, BotActions> getStep(String className, String stepName);

    void putStep(String className, String stepName, BiFunction<BotMessage, ? super UserContext, BotActions> method);
}
