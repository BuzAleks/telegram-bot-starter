package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuSectionHandler;
import link.buzalex.api.BotMenuStepActionsHolder;
import link.buzalex.api.BotMenuStepActionsProcessor;
import link.buzalex.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BotMenuStepActionsProcessorImpl implements BotMenuStepActionsProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BotMenuStepActionsProcessorImpl.class);

    private final BotApiService apiService;
    private final BotMenuStepActionsHolder stepsHolder;
    private final Map<String, BotMenuSectionHandler> menuHandlers;

    public BotMenuStepActionsProcessorImpl(BotApiService apiService, BotMenuStepActionsHolder stepsHolder, Map<String, BotMenuSectionHandler> menuHandlers) {
        this.apiService = apiService;
        this.stepsHolder = stepsHolder;
        this.menuHandlers = menuHandlers;
    }

    @Override
    public void processStep(BotMessage message, UserContext user) {


        if (user.getMenuSection() == null) {
            Integer minOrder = null;
            for (RootBotAction botAction : stepsHolder.getRootActions()) {
                if (botAction.getSelector().test(message)) {
                    if (minOrder == null || botAction.getOrder() < minOrder) {
                        minOrder = botAction.getOrder();
                        user.setMenuSection(botAction.name());
//                        user.getMenuSteps().push(botAction.name());
                        processCurrentStep(botAction, message, user);
                        return;
                    }
                }
            }
        }
        if (user.getMenuSection() == null) {
            LOG.warn("Menu section cannot be determined");
            return;
        }

        String prevStep = user.getMenuSteps().peekLast();


        if (prevStep != null) {
            BotAction stepActions = stepsHolder.getStepActions(user.getMenuSection(), prevStep);
            processPrevStep(stepActions, message, user);
        }

        // TODO: 16.01.2024 find out how to process current step
        String currentStep = user.getMenuSteps().peekLast();

//        if (currentStep!=null){
//            BotAction stepActions = stepsHolder.getStepActions(user.getMenuSection(), currentStep);
//            processCurrentStep(stepActions, message, user);
//        }
    }

    private void processCurrentStep(BotAction actions, BotMessage botMessage, UserContext user) {
        processReplies(botMessage, actions.replies());
    }

    private void processPrevStep(BotAction actions, BotMessage botMessage, UserContext userContext) {
        for (ConditionalActions conditionalAction : actions.conditionalActions()) {
            if (conditionalAction.condition().test(botMessage)) {
                processReplies(botMessage, conditionalAction.replies());
                userContext.getMenuSteps().push(conditionalAction.nextStepName());
                if (conditionalAction.finish()) {
                    userContext.setMenuSection(null);
                    userContext.getMenuSteps().clear();
                }
            }
        }
        if (actions.nextStepName()!=null){
            userContext.getMenuSteps().push(actions.nextStepName());
        }
        if (actions.finish()) {
            userContext.setMenuSection(null);
            userContext.getMenuSteps().clear();
        }
        if (actions.saveAs()!=null){
            userContext.putData(actions.saveAs(), botMessage.text());
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
