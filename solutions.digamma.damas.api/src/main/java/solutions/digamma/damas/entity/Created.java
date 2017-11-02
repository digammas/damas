package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;
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
     * @throws WorkspaceException
     */
    @NotNull
    String getCreatedBy() throws WorkspaceException;

    /**
     * Creation timestamp.
     *
     * @return
     * @throws WorkspaceException
     */
    @NotNull
    Calendar getCreationDate() throws WorkspaceException;
}
