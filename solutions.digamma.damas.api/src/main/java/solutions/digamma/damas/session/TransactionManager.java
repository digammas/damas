package solutions.digamma.damas.session;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.Token;

public interface TransactionManager {

    Transaction begin(Token token) throws WorkspaceException;
}
