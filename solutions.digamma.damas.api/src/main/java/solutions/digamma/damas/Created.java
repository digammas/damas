package solutions.digamma.damas;

import solutions.digamma.damas.inspection.Nonnull;

import java.util.Calendar;

/**
 * @author Ahmad Shahwan
 */
public interface Created {

    /**
     * Creator ID.
     *
     * @return
     * @throws DocumentException
     */
    @Nonnull
    String getCreatedBy() throws DocumentException;

    /**
     * Creation timestamp.
     *
     * @return
     * @throws DocumentException
     */
    @Nonnull
    Calendar getCreationDate() throws DocumentException;
}

