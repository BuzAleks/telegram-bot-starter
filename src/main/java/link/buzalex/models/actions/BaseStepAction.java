package link.buzalex.models.actions;

public class BaseStepAction {
    public String getName() {
        return name;
    }

    String name;

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "BaseStepAction{" +
                "name='" + name + '\'' +
                '}';
    }
}
