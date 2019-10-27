package solutions.digamma.damas.logging;

import javax.inject.Inject;
import javax.interceptor.InvocationContext;

/**
 * Abstract log interceptor.
 *
 * @author Ahmad Shahwan
 */
abstract class LogInterceptor {

    @Inject
    private Logbook log;

    /**
     * Log invocation and proceed.
     *
     * @param context   invocation context
     * @param before    pattern used to generate log message before invocation
     * @param after     pattern used to generate log message after invocation
     * @return          object returned by invocation
     * @throws Exception    exception thrown by invocation
     */
    Object proceed(
            InvocationContext context,
            String before,
            String after)
            throws Exception {
        String name = this.readName(context);
        this.log.info(before, name);
        Object object = this.proceed(context);
        this.log.info(after, name);
        return object;
    }

    private Object proceed(InvocationContext context)
            throws Exception {
        String name = this.readName(context);
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

    abstract protected String readName(InvocationContext context);
}
