package link.buzalex.models.action;

import link.buzalex.models.actions.BaseStepAction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface ActionsContainer {
    void put(String name, BaseStepAction action);

    void put(ActionsContainer action);

    BaseStepAction get(String name);

    BaseStepAction getFirst();

    BaseStepAction getNext(String name);

    List<BaseStepAction> values();

    Map<String, BaseStepAction> getMap();

    LinkedList<String> getOrder();

    boolean containsKey(String name);


}
