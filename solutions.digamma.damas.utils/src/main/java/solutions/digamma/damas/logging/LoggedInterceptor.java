package solutions.digamma.damas.logging;

import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

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
     */
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        return this.proceed(context, this::readMethodName,
                "Calling %s.", "Method %s returned successfully.");
    }

    /**
     * Intercept and log bean initialization.
     */
    @PostConstruct
    public Object initialize(InvocationContext context) throws Exception {
        return this.proceed(context, this::readTypeName,
                "Initializing bean of type %s.",
                "Bean of type %s initialized successfully.");
    }

    /**
     * Intercept and log bean disposal.
     */
    @PreDestroy
    public Object dispose(InvocationContext context) throws Exception {
        return this.proceed(context, this::readTypeName,
                "Disposing bean of type %s.",
                "Bean of type %s disposed successfully.");
    }

    /**
     * Log invocation and proceed.
     *
     * @param context   invocation context
     * @param readName  function to read name from context
     * @param before    pattern used to generate log message before invocation
     * @param after     pattern used to generate log message after invocation
     * @return          object returned by invocation
     * @throws Exception    exception thrown by invocation
     */
    private Object proceed(
            InvocationContext context,
            Function<InvocationContext, String> readName,
            String before,
            String after)
            throws Exception {
        String name = readName.apply(context);
        this.log.info(before, name);
        Object object = this.proceed(context);
        this.log.info(after, name);
        return object;
    }

    private Object proceed(InvocationContext context)
            throws Exception {
        String name = this.readMethodName(context);
        try {
            return context.proceed();
        } catch (RuntimeException e) {
            this.log.severe(e, "@%s threw unchecked exception.", name);
            throw e;
        } catch (Exception e) {
            this.log.info("@%s threw checked exception.", name);
            throw e;
        }
    }

    private String readMethodName(InvocationContext context) {
        return context.getClass().getName()
                .concat("::")
                .concat(context.getMethod().getName());
    }

    private String readTypeName(InvocationContext context) {
        return context.getClass().getName();
    }
}
