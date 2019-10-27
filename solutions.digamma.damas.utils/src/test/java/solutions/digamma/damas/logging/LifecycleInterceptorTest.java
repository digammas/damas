package solutions.digamma.damas.logging;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

@RunWith(ContainerRunner.class)
public class LifecycleInterceptorTest {

    @Inject
    private LifecycleInterception lifecycleInterception;

    @Inject
    private TypeLevelAllInterception typeLevelAllInterception;

    @Inject
    private Logger logger;

    @Test
    public void lifecycleInterceptionTest() {
        this.lifecycleInterception.doSomething();
        Arrays.stream(this.logger.getHandlers()).forEach(Handler::flush);
        assert !StreamHandlerProvider.OUT.toString()
                .contains("LifecycleInterception::doSomething");
        assert StreamHandlerProvider.OUT.toString()
                .contains("LifecycleInterception");
    }

    @Test
    public void typeLevelAllInterceptionTest() {
        this.typeLevelAllInterception.doSomething();
        Arrays.stream(this.logger.getHandlers()).forEach(Handler::flush);
        assert StreamHandlerProvider.OUT.toString()
                .contains("TypeLevelAllInterception::doSomething");
        assert StreamHandlerProvider.OUT.toString().split("TypeLevelAllInterception").length > 2;
    }

}