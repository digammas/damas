package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;

/**
 * A connection is a set of operations that are executed with the same user
 * session. A part from login, a valid connection is required for all workspace
 * operations.
 *
 * To create a connection, a valid used token is required, thus a valid login.
 * An instance of this class can only be obtained by calling the method {@code
 * connect} of a {@link ConnectionManager}.
 *
 * Once a connection is created, workspace methods called in the same thread
 * are done in behalf of the user session that created the connection, until the
 * connection is closed or another connection is created.
 *
 * Connections are closable. The user is encourage to close the connection as
 * soon as possible to avoid security breaches. The best way to use connections
 * is as a resource in a try-with-resource structure.
 *
 * A valid token (thus a valid user session) can be used to create as many
 * connections as needed, as long as those connections are property closed.
 *
 * A connection can be committed or rolled back as many times as needed.
 */
public interface Connection extends AutoCloseable {

    /**
     * Commit connection, persisting all changed in the user session.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void commit() throws WorkspaceException;

    /**
     * Rollback connection, resetting session data to the persisted state.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void rollback() throws WorkspaceException;

    /**
     * Close connection.
     *
     * @throws WorkspaceException   when a workspace exception occurs
     */
    void close() throws WorkspaceException;
}
