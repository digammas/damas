package solutions.digamma.damas.logging;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intercept and log method invocations.
 *
 * @author Ahmad Shahwan
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Logged {

    /**
     * Intercept and log lifecycle events.
     */
    @Inherited
    @InterceptorBinding
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Lifecycle {}

    /**
     * Intercept and log method invocations and lifecycle events.
     */
    @Inherited
    @InterceptorBinding
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Logged
    @Logged.Lifecycle
    @interface All {}
}
