package link.buzalex.models.actions;

import java.util.Objects;

public final class ReuseStepsChainAction extends BaseStepAction {
    private final String stepName;

    public ReuseStepsChainAction(String stepName) {
        this.stepName = stepName;
    }

    public String stepName() {
        return stepName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ReuseStepsChainAction) obj;
        return Objects.equals(this.stepName, that.stepName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepName);
    }
}
