package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;

public interface Transaction extends AutoCloseable {

    void commit() throws WorkspaceException;

    void rollback() throws WorkspaceException;

    void close() throws WorkspaceException;
}
