package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;

/**
 * A transaction is a set of operations that are executed with the same user
 * session. A part from login, a transaction is required for all workspace
 * operations.
 *
 * To begin a transaction, a valid used token is required, thus a valid login.
 * An instance of this class can only be obtained by calling the method <code>
 * begin</code> of a <code>TransactionManager</code>.
 *
 * Once a transaction has begun, workspace methods called in the current thread
 * are done in behalf of the user session that began the transaction, until the
 * transaction is closed or another transaction is begun.
 *
 * Transactions are closable. The user is encourage to close the transaction as
 * soon as possible to avoid security breaches. The best way to use transactions
 * is as a resource in a try-with-resource structure.
 *
 * A valid toke (thus a valid user session) can be used to begin as many
 * transactions as needed, as long as those transactions are property closed.
 *
 * A transaction can be committed or rolled back as many times as needed.
 */
public interface Transaction extends AutoCloseable {

    /**
     * Commit transaction, persisting all changed in the user session.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void commit() throws WorkspaceException;

    /**
     * Rollback transaction, resetting session data to the persisted state.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void rollback() throws WorkspaceException;

    /**
     * Close transaction.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void close() throws WorkspaceException;
}
