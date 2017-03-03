package solutions.digamma.damas.inspection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotated types cannot be null.
 *
 * Created by ahmad on 24/12/16.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Nonnull {
}
