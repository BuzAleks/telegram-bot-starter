package link.buzalex.impl;

import link.buzalex.api.BotApiService;
import link.buzalex.api.BotMenuStepProcessor;
import link.buzalex.models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BotMenuStepProcessorImpl implements BotMenuStepProcessor {
    private final BotApiService apiService;

    public BotMenuStepProcessorImpl(BotApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void beforeStepExecution(BotMessage botMessage, UserContext userContext) {
        final BotActions actions = userContext.getActions();
        if (actions != null) {
            for (BotActionsBuilder.ConditionalActions conditionalAction : actions.conditionalActions()) {
                if (conditionalAction.getCondition().test(botMessage)) {
                    processReplies(botMessage, conditionalAction.getReplies());
                    userContext.setCurrentStep(conditionalAction.getNextStep());
                }
            }
            userContext.putData(actions.paramName(), botMessage.text());
        }
    }

    @Override
    public void afterStepExecution(BotMessage botMessage, UserContext userContext, BotActions actions) {
        userContext.setActions(actions);
        processReplies(botMessage, actions.replies());
        userContext.setCurrentStep(actions.nextStep());
    }

    private void processReplies(BotMessage botMessage, Map<Long, List<BotMessageReply>> replies) {
        for (Map.Entry<Long, List<BotMessageReply>> replyEntry : replies.entrySet()) {
            Long userId = replyEntry.getKey() == null ? botMessage.userId() : replyEntry.getKey();
            for (BotMessageReply botMessageReply : replyEntry.getValue()) {
                apiService.sendToUser(botMessageReply.text(), userId);
            }
        }
    }
}
