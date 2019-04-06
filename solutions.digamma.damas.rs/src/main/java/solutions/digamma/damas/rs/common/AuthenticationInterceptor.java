package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.session.Transaction;
import solutions.digamma.damas.session.TransactionManager;
import solutions.digamma.damas.login.Token;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Authenticated
public class AuthenticationInterceptor {

    @Inject
    private TransactionManager manager;

    private Transaction begin(Token token) throws WorkspaceException {
        return this.manager.begin(token);
    }

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        if (!(context.getTarget() instanceof BaseResource)) {
            /* Not applicable, no authentication is done */
            return context.proceed();
        }
        BaseResource resource = (BaseResource) context.getTarget();
        try (Transaction transaction = begin(resource.getToken())) {
            Object returned = context.proceed();
            transaction.commit();
            return returned;
        }
    }
}
