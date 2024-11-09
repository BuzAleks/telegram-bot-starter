package link.buzalex.models.action;

import link.buzalex.models.actions.BaseStepAction;

import java.util.*;

public class ActionsContainerImpl implements ActionsContainer {
    private final LinkedHashMap<String, BaseStepAction> map = new LinkedHashMap<>();
    private final LinkedList<String> order = new LinkedList<>();

    @Override
    public void put(String name, BaseStepAction action) {
        map.put(name, action);
        order.add(name);
    }

    @Override
    public void put(ActionsContainer action) {
        map.putAll(action.getMap());
        order.addAll(action.getOrder());
    }

    @Override
    public BaseStepAction get(String name) {
        return map.get(name);
    }

    @Override
    public BaseStepAction getFirst() {
        return map.get(order.getFirst());
    }

    @Override
    public BaseStepAction getNext(String name) {
        if (name==null) return null;
        int index = order.indexOf(name);
        if (index == -1 || index + 1 >= order.size()) {
            return null;
        }
        String nextKey = order.get(index + 1);
        if (!map.containsKey(nextKey)) return null;
        return map.get(nextKey);
    }

    @Override
    public List<BaseStepAction> values() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Map<String, BaseStepAction> getMap() {
        return map;
    }
    @Override
    public LinkedList<String> getOrder() {
        return order;
    }

    @Override
    public boolean containsKey(String name) {
        return map.containsKey(name);
    }

    @Override
    public String toString() {
        return values().toString();
    }
}
