package link.buzalex.impl;

import link.buzalex.api.UserContextStorage;
import link.buzalex.api.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnMissingBean(value = UserContextStorage.class, ignored = InMemoryUserContextStorageImpl.class)
public class InMemoryUserContextStorageImpl implements UserContextStorage<UserContext> {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserContextStorageImpl.class);

    private Map<Long, UserContext> userMap = new HashMap<>();

    @Override
    public void saveUser(UserContext userContext) {
        LOG.debug("User saved: "+userContext.getId());
        userMap.put(userContext.getId(), userContext);
    }

    @Override
    public UserContext getUser(Long id) {
        return userMap.get(id);
    }
}
