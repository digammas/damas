package solutions.digamma.damas.logging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Lifecycle log interceptor.
 *
 * @author Ahmad Shahwan
 */
@Logged.Lifecycle
@Interceptor
public class LifecycleInterceptor extends LogInterceptor {

    /**
     * No-args constructor.
     */
    public LifecycleInterceptor() {
    }

    /**
     * Intercept and log bean initialization.
     */
    @PostConstruct
    public Object initialize(InvocationContext context) throws Exception {
        return this.proceed(context,
                "Initializing bean of type %s.",
                "Bean of type %s initialized successfully.");
    }

    /**
     * Intercept and log bean disposal.
     */
    @PreDestroy
    public Object dispose(InvocationContext context) throws Exception {
        return this.proceed(context,
                "Disposing bean of type %s.",
                "Bean of type %s disposed successfully.");
    }

    @Override
    protected String readName(InvocationContext context) {
        return context.getClass().getName();
    }
}
