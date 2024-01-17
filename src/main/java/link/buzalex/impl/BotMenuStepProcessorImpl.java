package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.api.BotMenuSectionsHolder;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.models.BotMessage;
import link.buzalex.models.BotMessageReply;
import link.buzalex.models.UserContext;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.ConditionalActions;
import link.buzalex.models.menu.MenuSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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
            user.setMenuSection(menuSection.name());
            user.getMenuSteps().push(menuSection.rootStepName());
            processCurrentStep(menuSection.steps().get(menuSection.rootStepName()), message, user);
            return;
        }

        if (user.getMenuSection() == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }

        String prevStep = user.getMenuSteps().peekLast();

        if (prevStep != null) {
            BotStep stepActions = stepsHolder.getStep(user.getMenuSection(), prevStep);
            processPrevStep(stepActions, message, user);
        }
    }

    private void processCurrentStep(BotStep actions, BotMessage botMessage, UserContext user) {
        processReplies(botMessage, actions.replies());
    }

    private void processPrevStep(BotStep actions, BotMessage botMessage, UserContext userContext) {
        String nextStepName = null;

        for (ConditionalActions conditionalAction : actions.conditionalActions()) {
            if (conditionalAction.condition().test(botMessage)) {
                processReplies(botMessage, conditionalAction.replies());
                nextStepName = conditionalAction.nextStepName();
                if (conditionalAction.finish()) {
                    userContext.setMenuSection(null);
                    userContext.getMenuSteps().clear();
                }
            }
        }

        if (actions.saveAs() != null) {
            userContext.putData(actions.saveAs(), botMessage.text());
        }

        if (nextStepName == null) {
            if (actions.finish()) {
                userContext.setMenuSection(null);
                userContext.getMenuSteps().clear();
            } else {
                nextStepName = actions.nextStepName();
            }
        }

        if (nextStepName != null) {
            userContext.getMenuSteps().push(nextStepName);
            BotStep stepActions = stepsHolder.getStep(userContext.getMenuSection(), nextStepName);
            processCurrentStep(stepActions, botMessage, userContext);
        }

        // TODO: 16.01.2024 add more processing
    }

    private void processReplies(BotMessage botMessage, Map<Long, List<BotMessageReply>> replies) {
        for (Map.Entry<Long, List<BotMessageReply>> replyEntry : replies.entrySet()) {
            Long userId = replyEntry.getKey() == 0L ? botMessage.userId() : replyEntry.getKey();
            for (BotMessageReply botMessageReply : replyEntry.getValue()) {
                apiService.sendToUser(botMessageReply.text(), userId);
            }
        }
    }
}
