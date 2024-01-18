package link.buzalex.api;

import java.util.List;
import java.util.Map;

public interface UserContext {
    Long getId();

    void setId(Long id);

    Map<String, String> getData();

    String getMenuSection();

    void setMenuSection(String menuSection);

    List<String> getMenuSteps();

    void setMenuSteps(List<String> menuSteps);
}
