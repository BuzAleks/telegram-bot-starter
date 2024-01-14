package link.buzalex.impl;

import link.buzalex.api.BotMenuStepsHolder;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.models.BotActions;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Component
public class BotMenuStepsHolderImpl implements BotMenuStepsHolder {

    Map<String, Map<String, BiFunction<BotMessage, ? super UserContext, BotActions>>> methods = new HashMap<>();

    @Override
    public BiFunction<BotMessage, ? super UserContext, BotActions> getStep(String className, String stepName) {
        final Map<String, BiFunction<BotMessage, ? super UserContext, BotActions>> particularClass = methods.get(className);
        if (particularClass == null) {
            throw new BotMenuStepMethodException("Couldn't find methods for class name " + className);
        }
        final BiFunction<BotMessage, ? super UserContext, BotActions> particularMethod = particularClass.get(stepName);
        if (particularMethod == null) {
            throw new BotMenuStepMethodException("Couldn't find method " + className + " for class name " + className);
        }
        return particularMethod;
    }

    @Override
    public void putStep(String className, String stepName, BiFunction<BotMessage, ? super UserContext, BotActions> method) {
        methods.computeIfAbsent(className, s -> new HashMap<>()).put(stepName, method);
    }
}
