package link.buzalex.api;

import link.buzalex.models.UserContext;

public interface UserContextStorage<T extends UserContext> {
    void saveUser(T userContext);

    T getUser(Long id);
}
