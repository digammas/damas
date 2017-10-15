package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;
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
     * @throws WorkspaceException
     */
    @NotNull
    String getModifiedBy() throws WorkspaceException;

    /**
     * Modification timestamp.
     *
     * @return
     * @throws WorkspaceException
     */
    @NotNull
    Calendar getModificationDate() throws WorkspaceException;
}
