package link.buzalex.models.context;

import link.buzalex.api.UserContext;
import link.buzalex.models.action.ActionStackItem;

import java.util.*;

public class UserContextImpl implements UserContext {
    private Long id;
    private Map<String, Object> data = new HashMap<>();
    private String entryPoint;
    private final Deque<ActionStackItem> actionsStack = new LinkedList<>();
    private final Deque<String> stepsHistory = new LinkedList<>();

    @Override
    public Deque<ActionStackItem> getStack() {
        return actionsStack;
    }

    @Override
    public Deque<String> getStepsHistory() {
        return stepsHistory;
    }

    public UserContextImpl() {
    }

    public UserContextImpl(Long id, String entryPoint) {
        this.id = id;
        this.entryPoint = entryPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void putData(String key, String value) {
        data.put(key, value);
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
