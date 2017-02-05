package solutions.digamma.damas.api.inspection;

import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotated value can be null.
 *
 * @auther Ahmad Shahwan
 */
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Nullable {
}
