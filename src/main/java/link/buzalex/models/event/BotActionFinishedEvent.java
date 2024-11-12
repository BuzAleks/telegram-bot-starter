package link.buzalex.models.event;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionCursor;

import java.util.Map;
import java.util.Objects;

public class BotActionFinishedEvent extends BotEvent {
    private final String entryPoint;
    private final String stepName;
    private final String actionName;
    private final Map<String, Object> contextData;

    public BotActionFinishedEvent(Object ref,
                                  Long userId,
                                  String entryPoint,
                                  String stepName,
                                  String actionName,
                                  Map<String, Object> contextData) {
        super(ref, userId);
        this.entryPoint = entryPoint;
        this.stepName = stepName;
        this.actionName = actionName;
        this.contextData = contextData;
    }

    public static BotActionFinishedEvent build(Object ref, UserContext context, ActionCursor cursor) {
        return new BotActionFinishedEvent(ref, context.getId(), context.getEntryPoint(), cursor.step().name(), cursor.getActionName(), context.getData());
    }

    public String entryPoint() {
        return entryPoint;
    }

    public String stepName() {
        return stepName;
    }

    public String actionName() {
        return actionName;
    }

    public Map<String, Object> contextData() {
        return contextData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BotActionFinishedEvent) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.entryPoint, that.entryPoint) &&
                Objects.equals(this.stepName, that.stepName) &&
                Objects.equals(this.actionName, that.actionName) &&
                Objects.equals(this.contextData, that.contextData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, entryPoint, stepName, actionName, contextData);
    }

    @Override
    public String toString() {
        return "BotActionFinishedEvent[" +
                "userId=" + userId + ", " +
                "entryPoint=" + entryPoint + ", " +
                "stepName=" + stepName + ", " +
                "actionName=" + actionName + ", " +
                "contextData=" + contextData + ']';
    }

}
