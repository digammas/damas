package solutions.digamma.damas.logging;

import java.lang.reflect.Method;
import javax.annotation.Priority;
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
@Priority(Interceptor.Priority.APPLICATION)
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
        Method method = context.getMethod();
        return method.getDeclaringClass().getName()
                .concat("::")
                .concat(method.getName());
    }
}
