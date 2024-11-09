package link.buzalex.api;

import link.buzalex.models.action.ActionStackItem;

import java.util.Deque;
import java.util.Map;

public interface UserContext {
    Deque<ActionStackItem> getStack();

    Long getId();

    void setId(Long id);

    Map<String, Object> getData();

    String getEntryPoint();

    void setEntryPoint(String entryPoint);
}
