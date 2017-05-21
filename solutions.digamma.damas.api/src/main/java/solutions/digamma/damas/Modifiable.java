package solutions.digamma.damas;

import solutions.digamma.damas.inspection.NotNull;

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
    @NotNull
    String getModifiedBy() throws DocumentException;

    /**
     * Modification timestamp.
     *
     * @return
     * @throws DocumentException
     */
    @NotNull
    Calendar getModificationDate() throws DocumentException;
}
