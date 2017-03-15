package solutions.digamma.damas.config;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fallback qualifier. String configuration qualified with this annotation
 * fallback to {@code value} if no configuration is found for the provided
 * key.
 *
 * @author Ahmad Shahwan
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Fallback {

    /**
     * Configuration key.
     *
     * @return
     */
    @Nonbinding String value();
}
