package solutions.digamma.damas.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.search.Filter;
import solutions.digamma.damas.search.SearchEngine;

/**
 * User management service.
 *
 * @author Ahmad Shahwan
 */
public interface UserManager
        extends CrudManager<User>, SearchEngine<User, Filter> {

    /**
     * Updates user's password.
     *
     * @param id            user's id
     * @param value      new password
     *
     * @throws solutions.digamma.damas.common.InvalidArgumentException
     *                      if the provided password doesn't meet minimum
     *                      requirements
     * @throws WorkspaceException
     */
    void updatePassword(String id, String value) throws WorkspaceException;
}
