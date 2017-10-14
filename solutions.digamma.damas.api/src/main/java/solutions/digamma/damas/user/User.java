package solutions.digamma.damas.user;


import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.Entity;

import java.util.List;

/**
 * System user.
 *
 * @author Ahmad Shahwan
 */
public interface User extends Entity {

    String getLogin() throws WorkspaceException;

    /**
     * Change user password.
     *
     * @param value                 new password
     * @throws WorkspaceException
     */
    void setPassword(String value) throws WorkspaceException;

    /**
     * User's email address.
     *
     * @return                      email address
     * @throws WorkspaceException
     */
    String getEmailAddress() throws WorkspaceException;

    /**
     * Set user's email address.
     *
     * @param value                 new email address.
     * @throws WorkspaceException
     */
    void setEmailAddress(String value) throws WorkspaceException;

    /**
     * List all group names to which the user belongs.
     *
     * @return                      list of group names
     * @throws WorkspaceException
     */
    List<String> getMemberships() throws WorkspaceException;
}
