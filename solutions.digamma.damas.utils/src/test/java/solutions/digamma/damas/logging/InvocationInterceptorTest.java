package solutions.digamma.damas.logging;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

@RunWith(ContainerRunner.class)
public class InvocationInterceptorTest {

    @Inject
    private TypeLevelInvocationInterception typeLevelInvocationInterception;

    @Inject
    private MethodLevelInvocationInterception methodLevelInvocationInterception;

    @Inject
    private Logger logger;

    @Test
    public void typeLevelInvocationInterceptionTest() {
        this.typeLevelInvocationInterception.doSomething();
        Arrays.stream(this.logger.getHandlers()).forEach(Handler::flush);
        assert StreamHandlerProvider.OUT.toString()
                .contains("TypeLevelInvocationInterception::doSomething");
    }

    @Test
    public void methodLevelInvocationInterceptionTest() {
        this.methodLevelInvocationInterception.doSomething();
        Arrays.stream(this.logger.getHandlers()).forEach(Handler::flush);
        assert StreamHandlerProvider.OUT.toString()
                .contains("MethodLevelInvocationInterception::doSomething");
    }
}