package solutions.digamma.damas.logging;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author Ahmad Shahwan
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Logged {
}
