package link.buzalex.models;

import java.util.HashMap;
import java.util.Map;

public class UserContext {
    private Long id;
    private final Map<String, String> data = new HashMap<>();
    private String currentMenuSection;
    private String currentStep;
    private BotActions actions;

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
}
