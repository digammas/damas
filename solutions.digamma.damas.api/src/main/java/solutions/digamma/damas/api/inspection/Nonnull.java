package solutions.digamma.damas.api.inspection;

import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotated types cannot be null.
 *
 * Created by ahmad on 24/12/16.
 */
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Nonnull {
}
