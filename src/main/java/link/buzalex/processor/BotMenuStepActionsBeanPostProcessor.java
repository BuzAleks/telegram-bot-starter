package link.buzalex.processor;

import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.api.BotMenuStepActionsHolder;
import link.buzalex.models.BotAction;
import link.buzalex.models.ConditionalActions;
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
    private BotMenuStepActionsHolder stepsHolder;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BotMenuSectionHandler) {
            BotAction actions = ((BotMenuSectionHandler<?>) bean).startStep();
            stepsHolder.putStepActions(beanName, "root", actions);
            collect(beanName, actions);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    private void collect(String beanName, BotAction actions) {
        stepsHolder.putStepActions(beanName, actions.name(), actions);
        for (ConditionalActions conditionalAction : actions.conditionalActions()) {
            if (!conditionalAction.finish()){
                collect(beanName, conditionalAction.nextStep());
            }
        }
        if (actions.finish()) return;
        collect(beanName, actions.nextStep());
    }
}
