package solutions.digamma.damas.config;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fallback configuration. String configuration qualified with this annotation
 * fallback to {@code value} if no configuration is found for the provided key.
 *
 * @author Ahmad Shahwan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Fallback {

    /**
     * Configuration default.
     *
     * @return
     */
    @Nonbinding String value();
}
