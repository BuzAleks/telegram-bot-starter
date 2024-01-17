package link.buzalex.processor;

import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.models.menu.MenuSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BotMenuStepActionsBeanPostProcessor implements BeanPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepActionsBeanPostProcessor.class);

    @Autowired
    private BotMenuSectionsHolder stepsHolder;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BotMenuSectionProvider) {
            MenuSection menuSection = ((BotMenuSectionProvider<?>) bean).provideMenuSection();
            stepsHolder.putMenuSection(menuSection);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
