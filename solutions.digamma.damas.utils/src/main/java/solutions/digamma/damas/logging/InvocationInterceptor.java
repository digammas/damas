package solutions.digamma.damas.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Invocation log interceptor.
 *
 * @author Ahmad Shahwan
 */
@Logged
@Interceptor
public class InvocationInterceptor extends LogInterceptor {

    /**
     * No-args constructor.
     */
    public InvocationInterceptor() {
    }

    /**
     * Intercept and log a method invocation.
     */
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        return this.proceed(context,
                "Calling %s.", "Method %s returned successfully.");
    }

    @Override
    protected String readName(InvocationContext context) {
        return this.readMethodName(context);
    }
}
