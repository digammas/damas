package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;

import java.time.ZonedDateTime;

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
    String getModifiedBy() throws WorkspaceException;

    /**
     * Modification timestamp.
     *
     * @return
     * @throws WorkspaceException
     */
    ZonedDateTime getModificationDate() throws WorkspaceException;
}
