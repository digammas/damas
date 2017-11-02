package solutions.digamma.damas.user;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;
import solutions.digamma.damas.inspection.NotNull;

/**
 * User management service.
 *
 * @author Ahmad Shahwan
 */
public interface UserManager extends CrudManager<User>, SearchEngine<User> {

    /**
     * Updates user's password.
     *
     * @param token         session token
     * @param id            user's id
     * @param value      new password
     *
     * @throws solutions.digamma.damas.common.InvalidArgumentException
     *                      if the provided password doesn't meet minimum
     *                      requirements
     * @throws WorkspaceException
     */
    void updatePassword(
            @NotNull Token token,
            @NotNull String id,
            @NotNull String value)
            throws WorkspaceException;
}