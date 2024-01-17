package link.buzalex.api;

public interface UserContextStorage<T extends UserContext> {
    void saveUser(T userContext);

    T getUser(Long id);
}
