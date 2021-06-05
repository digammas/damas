package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.Token;

/**
 * Connection manager.
 *
 * A service class used to create a new connection.
 */
public interface ConnectionManager {

    /**
     * Initiate a new connection.
     *
     * This method is the only way to acquire a new valid workspace connection.
     *
     * To create a new connection a valid (authenticated) token is required.
     *
     * @param token                 authentication token
     * @return                      workspace connection
     * @throws WorkspaceException   when a workspace exception occurs
     */
    Connection connect(Token token) throws WorkspaceException;
}
