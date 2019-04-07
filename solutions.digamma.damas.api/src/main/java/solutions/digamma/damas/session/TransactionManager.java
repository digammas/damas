package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.Token;

/**
 * Transaction manager.
 *
 * A service class used to create and begin transactions.
 */
public interface TransactionManager {

    /**
     * Instantiate and begin a transaction.
     *
     * This method is the only way to acquire a valid workspace transaction.
     *
     * To create a new transaction a valid (authenticated) token is required.
     *
     * @param token                 authentication token
     * @return                      workspace transaction
     * @throws WorkspaceException   when a workspace exception occurs
     */
    Transaction begin(Token token) throws WorkspaceException;
}
