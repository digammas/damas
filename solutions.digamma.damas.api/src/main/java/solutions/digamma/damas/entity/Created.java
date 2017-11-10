package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;

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
    String getCreatedBy() throws WorkspaceException;

    /**
     * Creation timestamp.
     *
     * @return
     * @throws WorkspaceException
     */
    Calendar getCreationDate() throws WorkspaceException;
}

