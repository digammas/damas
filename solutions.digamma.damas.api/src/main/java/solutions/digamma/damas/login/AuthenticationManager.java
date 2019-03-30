package solutions.digamma.damas.login;

import solutions.digamma.damas.common.WorkspaceException;

public interface AuthenticationManager {

    Authentication authenticate(Token token) throws WorkspaceException;
}
