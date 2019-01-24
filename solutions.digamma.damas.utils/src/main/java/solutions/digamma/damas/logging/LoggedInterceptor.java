package solutions.digamma.damas.logging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * @author Ahmad Shahwan
 */
@Logged
@Interceptor
public class LoggedInterceptor {

    @Inject
    private Logbook log;

    /**
     * No-args constructor.
     */
    public LoggedInterceptor() {
    }

    /**
     * Intercept and log a method invocation.
     *
     * @param context
     * @return
     * @throws Exception
     */
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        String name = "%s::%s".format(
                context.getClass().getName(),
                context.getMethod().getName());
        try {
            this.log.info(() -> String.format(
                    "Calling %s.", name));
            Object object = context.proceed();
            this.log.info(() -> String.format(
                    "Method %s returned successfully.", name));
            return object;
        } catch (RuntimeException e) {
            throw sever(name, e);
        } catch (Exception e) {
            throw info(name, e);
        }
    }

    /**
     * Intercept and log bean initialization.
     *
     * @param context
     * @return
     * @throws Exception
     */
    @PostConstruct
    public Object initialize(InvocationContext context) throws Exception {
        String name = context.getMethod().getName();
        try {
            this.log.info(() -> String.format(
                    "Initializing bean of type %s.", name));
            Object object = context.proceed();
            this.log.info(() -> String.format(
                    "Bean of type %s initialized successfully.", name));
            return object;
        } catch (RuntimeException e) {
            throw sever(name, e);
        } catch (Exception e) {
            throw info(name, e);
        }
    }

    /**
     * Intercept and log bean disposal.
     *
     * @param context
     * @return
     * @throws Exception
     */
    @PreDestroy
    public Object dispose(InvocationContext context) throws Exception {
        String name = context.getMethod().getName();
        try {
            this.log.info(() -> String.format(
                    "Disposing bean of type %s.", name));
            Object object = context.proceed();
            this.log.info(() -> String.format(
                    "Bean of type %s disposed successfully.", name));
            return object;
        } catch (RuntimeException e) {
            throw sever(name, e);
        } catch (Exception e) {
            throw info(name, e);
        }
    }

    private Exception sever(String name, Exception e) {
        this.log.log(Level.SEVERE, e, () -> String.format(
                "Unchecked exception @%s.", name));
        return e;
    }

    private Exception info(String name, Exception e) {
        this.log.info(() -> String.format(
                "Checked exception: \"%s\" @%s.", e.getMessage(), name));
        return e;
    }
}
