package solutions.digamma.damas.jaas;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JAAS realm, also called policy name, qualifier.
 *
 * JAAS {@link javax.security.auth.login.Configuration} implementations that are
 * annotated with this qualifier can handle realms identified by values of
 * {@code value()} member.
 *
 * @author Ahmad Shahwan
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface Realm {

    /**
     * Realm names, also referred to as policy names.
     *
     * @return
     */
    @Nonbinding String[] value();
}
