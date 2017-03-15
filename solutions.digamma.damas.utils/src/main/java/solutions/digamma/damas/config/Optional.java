package solutions.digamma.damas.config;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional configuration qualifier. Configuration qualified with this
 * annotation fallback to {@code null} if no configuration is found for the
 * provided key.
 *
 * @author Ahmad Shahwan
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Optional {
}
