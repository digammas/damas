package solutions.digamma.damas.api;

import solutions.digamma.damas.api.inspection.Nonnull;

import java.util.Calendar;

/**
 * @author Ahmad Shahwan
 */
public interface Created {

    /**
     * Creator ID.
     *
     * @return
     */
    @Nonnull
    String getCreatedBy();

    /**
     * Creation timestamp.
     *
     * @return
     */
    @Nonnull
    Calendar getCreationDate();
}

