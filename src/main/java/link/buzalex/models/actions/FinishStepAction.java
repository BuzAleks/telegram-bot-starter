package link.buzalex.models.actions;

public class FinishStepAction extends BaseStepAction{
    private final String nextStep;

    public FinishStepAction(String nextStep) {
        this.nextStep = nextStep;
    }

    public FinishStepAction() {
        this(null);
    }

    public String nextStep() {
        return nextStep;
    }
}
