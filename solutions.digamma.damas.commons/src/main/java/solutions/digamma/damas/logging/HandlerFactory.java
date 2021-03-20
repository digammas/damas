package solutions.digamma.damas.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Default log handler factory.
 */
@Singleton
public class HandlerFactory {

    private Handler handler = new ConsoleHandler();

    /**
     * Provide console log handler.
     *
     * @return  console log handler
     */
    @Produces
    public Handler getLogHandler() {
        return this.handler;
    }

    @PreDestroy
    public void dispose() {
        this.handler.close();
    }
}
