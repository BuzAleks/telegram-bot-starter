package link.buzalex.processor;

import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.models.menu.BotMenuEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BotMenuEntryPointBeanPostProcessor implements BeanPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuEntryPointBeanPostProcessor.class);

    @Autowired
    private BotMenuSectionsHolder stepsHolder;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BotMenuEntryPoint botMenuEntryPoint) {
            stepsHolder.putMenuSection(botMenuEntryPoint);
            LOG.debug("Added menu entrypoint: [{}] with steps: {}", botMenuEntryPoint.name(), botMenuEntryPoint.steps().keySet());
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
