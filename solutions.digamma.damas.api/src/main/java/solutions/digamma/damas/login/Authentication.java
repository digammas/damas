package solutions.digamma.damas.login;

import solutions.digamma.damas.common.WorkspaceException;

public interface Authentication extends AutoCloseable {

    void close() throws WorkspaceException;
}
