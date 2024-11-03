package link.buzalex.models.context;

import link.buzalex.api.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserContextImpl implements UserContext {
    private Long id;
    private Map<String, String> data = new HashMap<>();
    private String menuSection;
    private List<String> menuSteps = new ArrayList<>();

    public UserContextImpl() {
    }

    public UserContextImpl(Long id, String menuSection) {
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

    public List<String> getMenuSteps() {
        return menuSteps;
    }

    public void setMenuSteps(List<String> menuSteps) {
        this.menuSteps = menuSteps;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
