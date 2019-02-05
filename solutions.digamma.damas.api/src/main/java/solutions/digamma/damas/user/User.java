package solutions.digamma.damas.user;


import solutions.digamma.damas.common.WorkspaceException;

import java.util.List;

/**
 * User entity.
 *
 * @author Ahmad Shahwan
 */
public interface User extends Subject {

    String getLogin() throws WorkspaceException;

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
     * User's first name.
     *
     * @return                      first name
     * @throws WorkspaceException
     */
    String getFirstName() throws WorkspaceException;

    /**
     * Set user's last name.
     *
     * @param value                 first name
     * @throws WorkspaceException
     */
    void setFirstName(String value) throws WorkspaceException;

    /**
     * User's last name.
     *
     * @return                      last name
     * @throws WorkspaceException
     */
    String getLastName() throws WorkspaceException;

    /**
     * Set user's last name.
     *
     * @param value
     * @throws WorkspaceException
     */
    void setLastName(String value) throws WorkspaceException;

    /**
     * List all group names to which the user belongs.
     *
     * @return                      list of group names
     * @throws WorkspaceException
     */
    List<String> getMemberships() throws WorkspaceException;
}
