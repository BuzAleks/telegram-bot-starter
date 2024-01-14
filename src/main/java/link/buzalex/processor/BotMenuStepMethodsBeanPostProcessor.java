package link.buzalex.processor;

import link.buzalex.api.BotMenuStepsHolder;
import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.exception.BotMenuStepMethodException;
import link.buzalex.impl.BotMenuManagerImpl;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class BotMenuStepMethodsBeanPostProcessor implements BeanPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepMethodsBeanPostProcessor.class);

    @Autowired
    private BotMenuStepsHolder stepsHolder;
    private final Map<String, Map<String, Method>> annotatedMethods = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BotMenuSectionHandler) {
            for (Method method : bean.getClass().getMethods()) {
                if (method.isAnnotationPresent(BotMenuStep.class)) {
                    final BotMenuStep annotation = method.getAnnotation(BotMenuStep.class);
                    annotatedMethods.computeIfAbsent(beanName, s -> new HashMap<>()).put(annotation.value(), method);
                }
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (annotatedMethods.containsKey(beanName)) {
            annotatedMethods.get(beanName).entrySet().forEach(entry -> {
                final Method method = entry.getValue();
                if (method.getParameterCount() != 2) {
                    throw new BotMenuStepMethodException("Step method should have 2 parameters");
                }
                final Parameter[] parameters = method.getParameters();
                if (!BotMessage.class.isAssignableFrom(parameters[0].getType())) {
                    throw new BotMenuStepMethodException("Step method 1 parameter should be BotMessage");
                }
                if (!UserContext.class.isAssignableFrom(parameters[1].getType())) {
                    throw new BotMenuStepMethodException("Step method 2 parameter should be UserContext");
                }

                BiConsumer<BotMessage, ? super UserContext> consumer = (BiConsumer<BotMessage, UserContext>) (botMessage, userContext) -> {
                    try {
                        method.invoke(bean, botMessage, userContext);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new BotMenuStepMethodException("Something went wrong while menu step invocation", e);
                    }
                };
                stepsHolder.putStep(beanName, entry.getKey(), consumer);
                LOG.info("Method added as menu step: "+ entry.getKey()+" for bean: "+beanName);
            });
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
