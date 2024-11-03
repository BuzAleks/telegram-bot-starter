package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.exception.BotItemInitializationException;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.menu.BotStep;
import link.buzalex.models.menu.BotStepsChain;
import link.buzalex.models.menu.ConditionalActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BotItemsInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(BotItemsInitializer.class);

    private final BotItemsHolder itemsHolder;
    private final Set<String> unreachableStepsChains = new HashSet<>();
    private final Set<String> nonExistedStepReference = new HashSet<>();
    private final Set<String> references = new HashSet<>();

    public BotItemsInitializer(BotItemsHolder itemsHolder) {
        this.itemsHolder = itemsHolder;
    }
    @EventListener(ContextRefreshedEvent.class)
    void postInit(){
        if (!unreachableStepsChains.isEmpty()){
            LOG.warn("There is unreachable steps [{}]", unreachableStepsChains);
        }
        if (!nonExistedStepReference.isEmpty()){
            throw new BotItemInitializationException("There is links to non-existing steps: "+ nonExistedStepReference);
        }
    }

    public void initEntryPoint(BotEntryPoint entryPoint) {
        if (entryPoint.stepsChain() != null){
            initStepsChain(entryPoint.stepsChain());
        }
        checkReference(entryPoint.rootStepName());
        BotEntryPoint botEntryPoint = entryPoint.convertToPlainEntryPoint();
        itemsHolder.putEntryPoint(botEntryPoint);
    }

    public void initStepsChain(BotStepsChain stepsChain) {
        checkUnreachableStep(stepsChain);
        collect(stepsChain);
    }

    private void collect(BotStepsChain step) {
        putStep(step.convertToPlainStep());
        LOG.debug("Step [{}] initialized", step.name());
        if (step.answerActions() != null) {
            for (ConditionalActions conditionalAction : step.answerActions().conditionalActions()) {
                if (conditionalAction.nextStep() == null) continue;
                collect(conditionalAction.nextStep());
            }
        }
        if (step.nextStep() == null) return;
        collect(step.nextStep());
    }

    private void putStep(BotStep step){
        if (itemsHolder.getStep(step.name())!=null){
            throw new BotItemInitializationException(String.format("Bot step name [%s] already exists", step.name()));
        }
        itemsHolder.putStep(step);
        nonExistedStepReference.remove(step.name());
    }

    private void checkReference(String stepName){
        references.add(stepName);
        unreachableStepsChains.remove(stepName);
        if (itemsHolder.getStep(stepName) == null){
            nonExistedStepReference.add(stepName);
        }
    }

    private void checkUnreachableStep(BotStepsChain stepsChain){
        if (!references.contains(stepsChain.name())){
            unreachableStepsChains.add(stepsChain.name());
        }
    }
}
