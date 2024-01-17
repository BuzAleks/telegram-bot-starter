package link.buzalex.models;

import java.util.*;

public class UserContext {
    private Long id;
    private Map<String, String> data = new HashMap<>();
    private String menuSection;
    private List<String> menuSteps = new ArrayList<>();

    public UserContext() {
    }

    public UserContext(Long id, String menuSection) {
        this.id = id;
        this.menuSection = menuSection;
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

    public String getMenuSection() {
        return menuSection;
    }

    public void setMenuSection(String menuSection) {
        this.menuSection = menuSection;
    }

    public Deque<String> getMenuSteps() {
        return new ArrayDeque<>(menuSteps);
    }

    public void setMenuSteps(Deque<String> menuSteps) {
        this.menuSteps = menuSteps.stream().toList();
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
