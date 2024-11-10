package link.buzalex.models.action;

import link.buzalex.models.actions.Action;

public class ActionsContainer {
    private String methodName;
    private Action action;
    private ActionsContainer nextAction;

    public ActionsContainer() {
    }

    public ActionsContainer(String methodName, Action action) {
        this.methodName = methodName;
        this.action = action;
    }

    public String getName() {
        return methodName;
    }

    public void setName(String methodName) {
        this.methodName = methodName;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public ActionsContainer getNextAction() {
        return nextAction;
    }

    public void setNextAction(ActionsContainer nextAction) {
        this.nextAction = nextAction;
    }

    @Override
    public String toString() {
        return "ActionsContainer{" +
                "name='" + methodName + '\'' +
                "next="+nextAction+
                '}';
    }
}
