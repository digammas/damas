package solutions.digamma.damas.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;

/**
 * A group of users.
 *
 * @author Ahmad Shahwan
 */
public interface Group extends Subject {

    /**
     * Group's unique name. Group name is mutable and different form its
     * identifier.
     *
     * @return                      group's name
     * @throws WorkspaceException
     */
    String getName() throws WorkspaceException;

    /**
     * Set group's name.
     *
     * @param value                 group's new name
     * @throws WorkspaceException
     */
    void setName(String value) throws WorkspaceException;
}
