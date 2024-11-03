package link.buzalex.configuration;

import link.buzalex.annotation.EntryPoint;
import link.buzalex.annotation.StepsChain;
import link.buzalex.impl.BotItemsInitializer;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStepsChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class BotItemsBeanPostProcessor implements BeanPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotItemsBeanPostProcessor.class);

    @Autowired
    private BotItemsInitializer itemsConfigurator;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EntryPoint.class)) {
                LOG.debug("Method {} annotated as EntryPoint found", method.getName());
                if (!BotEntryPoint.class.isAssignableFrom(method.getReturnType()) || method.getParameterCount() > 0) {
                    throw new IllegalArgumentException(
                            "Method " + method.getName() + " in " + bean.getClass().getName()
                                    + " must return BotEntryPoint and have no parameters"
                    );
                }
                try {
                    method.setAccessible(true);
                    BotEntryPoint entryPoint = (BotEntryPoint) method.invoke(bean);
                    throwIfNull(entryPoint, method.getName(), beanName);
                    itemsConfigurator.initEntryPoint(entryPoint);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke method: " + method.getName(), e);
                }
            } else if (method.isAnnotationPresent(StepsChain.class)) {
                LOG.debug("Method {} annotated as StepsChain found", method.getName());
                if (!BotStepsChain.class.isAssignableFrom(method.getReturnType()) || method.getParameterCount() > 0) {
                    throw new IllegalArgumentException(
                            "Method " + method.getName() + " in " + bean.getClass().getName()
                                    + " must return BotEntryPoint and have no parameters"
                    );
                }
                try {
                    method.setAccessible(true);
                    BotStepsChain stepsChain = (BotStepsChain) method.invoke(bean);
                    throwIfNull(stepsChain, method.getName(), beanName);
                    itemsConfigurator.initStepsChain(stepsChain);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke method: " + method.getName(), e);
                }
            }
        }
        return bean;
    }
    private void throwIfNull(Object object, String methodName, String className){
        if (object == null) {
            throw new RuntimeException("Method " + methodName + " in " + className + " returned null");
        }
    }
}
