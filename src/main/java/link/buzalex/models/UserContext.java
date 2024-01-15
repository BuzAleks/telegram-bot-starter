package link.buzalex.models;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class UserContext {
    private Long id;
    private Map<String, String> data = new HashMap<>();
    private String currentMenuSection;
    private String currentStep;
    private BotActions actions;
    private BiFunction<BotMessage, ? super UserContext, BotActions> stepFunc;


    public UserContext() {
    }

    public UserContext(Long id, String currentMenuSection, String currentStep, BotActions actions, BiFunction<BotMessage, ? super UserContext, BotActions> stepFunc) {
        this.id = id;
        this.currentMenuSection = currentMenuSection;
        this.currentStep = currentStep;
        this.actions = actions;
        this.stepFunc = stepFunc;
    }

    public BiFunction<BotMessage, ? super UserContext, BotActions> getStepFunc() {
        return stepFunc;
    }

    public void setStepFunc(BiFunction<BotMessage, ? super UserContext, BotActions> stepFunc) {
        this.stepFunc = stepFunc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void putData(String key, String value) {
        data.put(key, value);
    }

    public String getCurrentMenuSection() {
        return currentMenuSection;
    }

    public void setCurrentMenuSection(String currentMenuSection) {
        this.currentMenuSection = currentMenuSection;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public BotActions getActions() {
        return actions;
    }

    public void setActions(BotActions actions) {
        this.actions = actions;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
