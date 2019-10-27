package solutions.digamma.damas.logging;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

@RunWith(ContainerRunner.class)
@Singleton
public class LoggerFactoryTest {

    private static final String TEST_MESSAGE = "Hello World";

    @Inject
    private Logger logger;

    @Test
    public void getLogger() {
        this.logger.info(TEST_MESSAGE);
        Arrays.stream(this.logger.getHandlers()).forEach(Handler::flush);
        assert StreamHandlerProvider.OUT.toString().contains(TEST_MESSAGE);
    }
}