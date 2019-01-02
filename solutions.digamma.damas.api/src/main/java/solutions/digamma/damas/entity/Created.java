package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;

import java.time.ZonedDateTime;

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
    String getCreatedBy() throws WorkspaceException;

    /**
     * Creation timestamp.
     *
     * @return
     * @throws WorkspaceException
     */
    ZonedDateTime getCreationDate() throws WorkspaceException;
}

