package solutions.digamma.damas.config;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration qualifier.
 *
 * @author Ahmad Shahwan
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface Configuration {

    /**
     * Configuration key.
     *
     * @return
     */
    @Nonbinding String[] value();

    @Nonbinding boolean optional() default false;
}
