package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.UserMessageContainer;
import link.buzalex.models.action.BaseStepAction;
import link.buzalex.models.action.ConditionalAction;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepProcessorImpl.class);

    private final BotMenuActionsExecutor actionsExecutor;
    private final BotItemsHolder stepsHolder;

    public BotMenuStepProcessorImpl(BotMenuActionsExecutor actionsExecutor, BotItemsHolder stepsHolder) {
        this.actionsExecutor = actionsExecutor;
        this.stepsHolder = stepsHolder;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {
        LOG.debug("Message processing is being started. Menu section: {}, steps: {}", user.getMenuSection(), user.getMenuSteps());
        String menuSection = determineMenuSection(message, user);
        if (menuSection == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }

        if (!user.getMenuSteps().isEmpty()) {
            String prevStep = user.getMenuSteps().get(user.getMenuSteps().size() - 1);
            BotStep step = stepsHolder.getStep(prevStep);
            String nextStep = processPrevStepAndReturnNext(step, message, user);
            if (nextStep != null) {
                pushNextStep(user, nextStep);
            } else {
                finishMenu(user);
            }
        } else {
            user.getMenuSteps().add(stepsHolder.getEntryPoint(menuSection).rootStepName());
        }

        if (!user.getMenuSteps().isEmpty()) {
            String currStep = user.getMenuSteps().get(user.getMenuSteps().size() - 1);
            BotStep step = stepsHolder.getStep(currStep);
            processCurrentStep(step, message, user);

            while (step.answerActions() == null) {
                if (step.nextStepName() == null) {
                    finishMenu(user);
                    break;
                } else {
                    pushNextStep(user, step.nextStepName());
                    step = stepsHolder.getStep(step.nextStepName());
                    processCurrentStep(step, message, user);
                }
            }

        }
    }

    private void pushNextStep(UserContext user, String nextStep) {
        List<String> menuSteps = user.getMenuSteps();
        menuSteps = menuSteps.stream().takeWhile(s -> s.equals(nextStep)).collect(Collectors.toList());
        if (menuSteps.size() != user.getMenuSteps().size()) {
            menuSteps.add(nextStep);
            user.setMenuSteps(menuSteps);
            LOG.debug("Steps rolled back to: {}", nextStep);
        } else {
            user.getMenuSteps().add(nextStep);
            LOG.debug("New step pushed: {}", nextStep);
        }
    }

    private void finishMenu(UserContext user) {
        LOG.debug("MenuSection [{}] finished with steps: {}", user.getMenuSection(), user.getMenuSteps());
        user.setMenuSection(null);
        user.getMenuSteps().clear();
    }

    private String determineMenuSection(BotMessage message, UserContext user) {
        BotEntryPoint botEntryPoint = null;
        if (user.getMenuSection() == null) {
            Integer minOrder = null;
            for (BotEntryPoint botAction : stepsHolder.getEntryPoints().values()) {
                if (botAction.selector().test(message)) {
                    if (minOrder == null || botAction.order() < minOrder) {
                        minOrder = botAction.order();
                        botEntryPoint = botAction;
                    }
                }
            }
        }
        if (botEntryPoint != null) {
            LOG.debug("Chosen menu section: {}", botEntryPoint.name());
            user.setMenuSection(botEntryPoint.name());
        }
        return user.getMenuSection();
    }

    private void processCurrentStep(BotStep actions, BotMessage botMessage, UserContext user) {
        LOG.debug("Current step is being processed: {}", actions.name());
        actionsExecutor.execute(botMessage, user, actions.stepActions());
    }

    private String processPrevStepAndReturnNext(BotStep actions, BotMessage botMessage, UserContext userContext) {
        LOG.debug("Previous step is being processed: {}", actions.name());

        String nextStepName = null;

        for (BaseStepAction action : actions.answerActions()) {
            if (action instanceof ConditionalAction conditionalAction){
                if (conditionalAction.getCondition().test(new UserMessageContainer(botMessage, userContext))) {
                    LOG.debug("Condition is being processed on step: {}", actions.name());
                    actionsExecutor.execute(botMessage, userContext, conditionalAction.getConditionalActions());
                    if (conditionalAction.getNextStep() == null) {
                        LOG.debug("Condition finished");
                    } else {
                        nextStepName = conditionalAction.getNextStep().name();
                    }
                    break;
                }
            }
        }


        return nextStepName == null ? actions.nextStepName() : nextStepName;
    }
}
