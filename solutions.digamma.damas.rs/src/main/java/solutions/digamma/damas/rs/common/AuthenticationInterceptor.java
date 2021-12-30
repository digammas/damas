package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.session.Connection;
import solutions.digamma.damas.session.ConnectionManager;
import solutions.digamma.damas.session.Token;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Authenticated
public class AuthenticationInterceptor {

    @Inject
    private ConnectionManager manager;

    private Connection begin(Token token) throws WorkspaceException {
        return this.manager.connect(token);
    }

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        if (!(context.getTarget() instanceof BaseResource)) {
            /* Not applicable, no authentication is done */
            return context.proceed();
        }
        BaseResource resource = (BaseResource) context.getTarget();
        /* Code should be compilable with Java 8 for Enunciate compatibility */
        Connection shadowConnection = null;
        try (Connection connection = begin(resource.getToken())) {
            shadowConnection = connection;
            Object returned = context.proceed();
            connection.commit();
            return returned;
        } catch (Throwable e) {
            if (shadowConnection !=  null) {
                try {
                    shadowConnection.rollback();
                } catch (Exception ignore) {
                    /* Exception when session not yet open, ignore it */
                }
            }
            throw e;
        }
    }
}
