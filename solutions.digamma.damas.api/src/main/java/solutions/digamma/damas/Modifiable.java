package solutions.digamma.damas;

import solutions.digamma.damas.inspection.Nonnull;

import java.util.Calendar;

/**
 * @author Ahmad Shahwan
 */
public interface Modifiable {

    /**
     * Modifier ID.
     *
     * @return
     * @throws DocumentException
     */
    @Nonnull
    String getModifiedBy() throws DocumentException;

    /**
     * Modification timestamp.
     *
     * @return
     * @throws DocumentException
     */
    @Nonnull
    Calendar getModificationDate() throws DocumentException;
}
