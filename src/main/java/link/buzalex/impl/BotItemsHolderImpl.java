package link.buzalex.impl;

import link.buzalex.api.BotItemsHolder;
import link.buzalex.models.menu.BotEntryPoint;
import link.buzalex.models.step.BotStep;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotItemsHolderImpl implements BotItemsHolder {
    private final Map<String, BotEntryPoint> entryPoints = new HashMap<>();

    private final Map<String, BotStep> botSteps = new HashMap<>();

    @Override
    public BotStep getStep(String stepName) {
        return botSteps.get(stepName);
    }

    @Override
    public void putStep(BotStep botStep) {
        botSteps.put(botStep.name(), botStep);
    }

    @Override
    public BotEntryPoint getEntryPoint(String entryPointName) {
        return entryPoints.get(entryPointName);
    }

    @Override
    public Map<String, BotEntryPoint> getEntryPoints() {
        return entryPoints;
    }

    @Override
    public void putEntryPoint(BotEntryPoint rootStep) {
        entryPoints.put(rootStep.name(), rootStep);
    }
}