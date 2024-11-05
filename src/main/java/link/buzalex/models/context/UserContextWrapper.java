package link.buzalex.models.context;

import link.buzalex.api.UserContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class UserContextWrapper {

    private final UserContext context;

    public UserContextWrapper(UserContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAs(Class<T> type, String key) {
        return Optional.ofNullable(context.getData().get(key))
                .map(obj -> {
                    try {
                        return (T) obj;
                    } catch (ClassCastException e) {
                        return null;
                    }
                });
    }

    public Optional<Integer> getAsInt(String key) {
        return Optional.ofNullable(context.getData().get(key))
                .map(obj -> {
                    if (obj instanceof Integer i) {
                        return i;
                    } else if (obj instanceof String s) {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                    return null;
                });
    }


    public Optional<String> getAsString(String key) {
        return Optional.ofNullable(context.getData().get(key))
                .map(obj -> (obj instanceof String) ? (String) obj : obj.toString());
    }


    public void put(String key, Object data) {
        context.getData().put(key, data);
    }

    public void put(Map<String, Object> data) {
        context.getData().putAll(data);
    }

    public void modifyIfPresents(String key, Function<Object, Object> func){
        Object obj = context.getData().get(key);
        if (obj != null){
            context.getData().put(key, func.apply(obj));
        }
    }

    public void modifyIfPresentsAsString(String key, Function<String, Object> func){
        Object obj = context.getData().get(key);
        if (obj != null){
            context.getData().put(key, func.apply(obj.toString()));
        }
    }

    public void modifyIfPresentsAsInt(String key, Function<Integer, Object> func){
        Object obj = context.getData().get(key);
        try {
            if (obj != null){
                int i = Integer.parseInt(obj.toString());
                context.getData().put(key, func.apply(i));
            }
        } catch (NumberFormatException ignored) {
        }
    }
}
