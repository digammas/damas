package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;

public interface Transaction extends AutoCloseable {

    void close() throws WorkspaceException;
}
