package link.buzalex.api;

import link.buzalex.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBotMenuSectionHandler<T extends UserContext> implements BotMenuSectionHandler<T> {
    private BotApiService botApiService;

    @Override
    public int order() {
        return 0;
    }

    public void sendToUser(String message, Long id, String parseMode) {
        botApiService.sendToUser(message, id, parseMode);
    }

    @Autowired
    protected void setBotApiService(BotApiService botApiService) {
        this.botApiService = botApiService;
    }
}
