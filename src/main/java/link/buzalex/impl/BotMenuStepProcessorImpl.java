package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.api.UserContext;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.ConditionalActions;
import link.buzalex.models.menu.MenuSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepProcessorImpl.class);

    private final BotApiService apiService;
    private final BotMenuSectionsHolder stepsHolder;

    public BotMenuStepProcessorImpl(BotApiService apiService, BotMenuSectionsHolder stepsHolder) {
        this.apiService = apiService;
        this.stepsHolder = stepsHolder;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {
        LOG.debug("Message processing is being started. Menu section: " + user.getMenuSection() + ", steps: " + user.getMenuSteps());
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
            user.getMenuSteps().add(menuSection.rootStepName());
            processCurrentStep(menuSection.steps().get(menuSection.rootStepName()), message, user);
            return;
        }

        if (user.getMenuSection() == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }

        String prevStep = user.getMenuSteps().get(user.getMenuSteps().size() - 1);

        if (prevStep != null) {
            BotStep stepActions = stepsHolder.getStep(user.getMenuSection(), prevStep);
            processPrevStep(stepActions, message, user);
        }
    }

    private void processCurrentStep(BotStep actions, BotMessage botMessage, UserContext user) {
        LOG.debug("Current step is being processed: " + actions.name());
        processReplies(botMessage, actions.stepActions().replies());
    }

    private void processPrevStep(BotStep actions, BotMessage botMessage, UserContext userContext) {
        LOG.debug("Previous step is being processed: " + actions.name());

        String nextStepName = null;

        if (actions.answerActions() != null) {
            for (ConditionalActions conditionalAction : actions.answerActions().conditionalActions()) {
                if (conditionalAction.condition().test(botMessage)) {
                    LOG.debug("Condition is being processed on step: " + actions.name());
                    processReplies(botMessage, conditionalAction.replies());
                    nextStepName = conditionalAction.nextStepName();
                    if (conditionalAction.nextStepName() == null) {
                        userContext.setMenuSection(null);
                        userContext.getMenuSteps().clear();
                        LOG.debug("Conditional step finished");
                        return;
                    }
                    break;
                }
            }
        }

        if (actions.answerActions() != null && actions.answerActions().saveAs() != null) {
            userContext.getData().put(actions.answerActions().saveAs(), botMessage.text());
        }

        if (nextStepName == null) {
            if (actions.nextStepName() == null) {
                userContext.setMenuSection(null);
                userContext.getMenuSteps().clear();
                LOG.debug("Step finished");
            } else {
                nextStepName = actions.nextStepName();
            }
        }

        if (nextStepName != null) {
            LOG.debug("Next step pushed: " + nextStepName);
            userContext.getMenuSteps().add(nextStepName);
            BotStep stepActions = stepsHolder.getStep(userContext.getMenuSection(), nextStepName);
            processCurrentStep(stepActions, botMessage, userContext);
        }
    }

    private void processReplies(BotMessage botMessage, Map<Long, List<Function<BotMessage, BotMessageReply>>> replies) {
        for (Map.Entry<Long, List<Function<BotMessage, BotMessageReply>>> replyEntry : replies.entrySet()) {
            Long userId = replyEntry.getKey() == 0L ? botMessage.userId() : replyEntry.getKey();
            for (Function<BotMessage, BotMessageReply> botMessageReply : replyEntry.getValue()) {
                final BotMessageReply apply = botMessageReply.apply(botMessage);
                apiService.sendToUser(apply.text(), userId);
            }
        }
    }
}
