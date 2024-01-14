package link.buzalex.impl;

import link.buzalex.api.BotMenuStepsHolder;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
@Component
public class BotMenuStepsHolderImpl implements BotMenuStepsHolder {

    Map<String, Map<String, BiConsumer<BotMessage, ? super UserContext>>> methods = new HashMap<>();

    @Override
    public BiConsumer<BotMessage, ? super UserContext> getStep(String className, String stepName) {
        final Map<String, BiConsumer<BotMessage, ? super UserContext>> particularClass = methods.get(className);
        if (particularClass == null) {
            throw new BotMenuStepMethodException("Couldn't find methods for class name " + className);
        }
        final BiConsumer<BotMessage, ? super UserContext> particularMethod = particularClass.get(stepName);
        if (particularMethod == null) {
            throw new BotMenuStepMethodException("Couldn't find method " + className + " for class name " + className);
        }
        return particularMethod;
    }

    @Override
    public void putStep(String className, String stepName, BiConsumer<BotMessage, ? super UserContext> method) {
        methods.computeIfAbsent(className, s -> new HashMap<>()).put(stepName, method);
    }
}
