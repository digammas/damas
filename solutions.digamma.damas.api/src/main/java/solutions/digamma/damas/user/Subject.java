package solutions.digamma.damas.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;

/**
 * Generalization of groups and users.
 *
 * @author Ahmad Shahwan
 */
public interface Subject extends Entity {

    /**
     * Whether user is enabled. Only enabled users are allowed to login.
     *
     * @return          {@code true} if user is enable, {@code false} otherwise
     * @throws WorkspaceException
     */
    Boolean isEnabled() throws WorkspaceException;

    /**
     * Enable or disable user.
     *
     * @param value     if {@code true} enable user, disable otherwise
     * @throws WorkspaceException
     */
    void setEnabled(Boolean value) throws WorkspaceException;
}
