package solutions.digamma.damas;

import solutions.digamma.damas.inspection.NotNull;

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
    @NotNull
    String getCreatedBy() throws DocumentException;

    /**
     * Creation timestamp.
     *
     * @return
     * @throws DocumentException
     */
    @NotNull
    Calendar getCreationDate() throws DocumentException;
}

