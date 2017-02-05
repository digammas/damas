package solutions.digamma.damas.api;

import solutions.digamma.damas.api.inspection.Nonnull;

import java.util.Calendar;

/**
 * @author Ahmad Shahwan
 */
public interface Modifiable {

    /**
     * Modifier ID.
     *
     * @return
     */
    @Nonnull
    String getModifiedBy();

    /**
     * Modification timestamp.
     *
     * @return
     */
    @Nonnull
    Calendar getModificationDate();
}
