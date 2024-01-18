package link.buzalex.impl;

import link.buzalex.api.BotMenuActionsExecutor;
import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.ConditionalActions;
import link.buzalex.models.menu.MenuSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepProcessorImpl.class);

    private final BotMenuActionsExecutor actionsExecutor;
    private final BotMenuSectionsHolder stepsHolder;

    public BotMenuStepProcessorImpl(BotMenuActionsExecutor actionsExecutor, BotMenuSectionsHolder stepsHolder) {
        this.actionsExecutor = actionsExecutor;
        this.stepsHolder = stepsHolder;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {
        LOG.debug("Message processing is being started. Menu section: " + user.getMenuSection() + ", steps: " + user.getMenuSteps());
        String menuSection = determineMenuSection(message, user);
        if (menuSection == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }

        if (user.getMenuSteps().size() > 0) {
            String prevStep = user.getMenuSteps().get(user.getMenuSteps().size() - 1);
            BotStep step = stepsHolder.getStep(menuSection, prevStep);
            String nextStep = processPrevStepAndReturnNext(step, message, user);
            if (nextStep != null) {
                pushNextStep(user, nextStep);
            } else {
                finishMenu(user);
            }
        } else {
            user.getMenuSteps().add(stepsHolder.getMenuSections().get(menuSection).rootStepName());
        }

        if (!user.getMenuSteps().isEmpty()) {
            String currStep = user.getMenuSteps().get(user.getMenuSteps().size() - 1);
            BotStep step = stepsHolder.getStep(menuSection, currStep);
            processCurrentStep(step, message, user);

            while (step.answerActions() == null) {
                if (step.nextStepName() == null) {
                    finishMenu(user);
                } else {
                    pushNextStep(user, step.nextStepName());
                    step = stepsHolder.getStep(menuSection, step.nextStepName());
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
            LOG.debug("Steps rolled back to: " + nextStep);
        } else {
            user.getMenuSteps().add(nextStep);
            LOG.debug("New step pushed: " + nextStep);
        }
    }

    private void finishMenu(UserContext user) {
        LOG.debug("MenuSection [" + user.getMenuSection() + "] finished with steps: " + user.getMenuSteps());
        user.setMenuSection(null);
        user.getMenuSteps().clear();
    }

    private String determineMenuSection(BotMessage message, UserContext user) {
        MenuSection menuSection = null;
        if (user.getMenuSection() == null) {
            Integer minOrder = null;
            for (MenuSection botAction : stepsHolder.getMenuSections().values()) {
                if (botAction.selector().test(message)) {
                    if (minOrder == null || botAction.order() < minOrder) {
                        minOrder = botAction.order();
                        menuSection = botAction;
                    }
                }
            }
        }
        if (menuSection != null) {
            LOG.debug("Chosen menu section: " + menuSection.name());
            user.setMenuSection(menuSection.name());
        }
        return user.getMenuSection();
    }

    private void processCurrentStep(BotStep actions, BotMessage botMessage, UserContext user) {
        LOG.debug("Current step is being processed: " + actions.name());
        actionsExecutor.execute(botMessage, user, actions.stepActions());
    }

    private String processPrevStepAndReturnNext(BotStep actions, BotMessage botMessage, UserContext userContext) {
        LOG.debug("Previous step is being processed: " + actions.name());

        String nextStepName = null;

        if (actions.answerActions() != null) {
            for (ConditionalActions conditionalAction : actions.answerActions().conditionalActions()) {
                if (conditionalAction.condition().test(botMessage)) {
                    LOG.debug("Condition is being processed on step: " + actions.name());
                    actionsExecutor.execute(botMessage, userContext, conditionalAction);
                    nextStepName = conditionalAction.nextStepName();
                    if (conditionalAction.nextStepName() == null) {
                        LOG.debug("Condition finished");
                        return null;
                    }
                    break;
                }
            }
            if (actions.answerActions().saveAs() != null) {
                userContext.getData().put(actions.answerActions().saveAs(), botMessage.text());
            }
        }

        return nextStepName == null ? actions.nextStepName() : nextStepName;
    }
}
