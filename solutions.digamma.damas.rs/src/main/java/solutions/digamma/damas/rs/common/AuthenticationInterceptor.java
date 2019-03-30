package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.Authentication;
import solutions.digamma.damas.login.AuthenticationManager;
import solutions.digamma.damas.login.Token;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Authenticated
public class AuthenticationInterceptor {

    @Inject
    private AuthenticationManager manager;

    private Authentication auth(Token token) throws WorkspaceException {
        return this.manager.authenticate(token);
    }

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        if (!(context.getTarget() instanceof BaseResource)) {
            /* Not applicable, no authentication is done */
            return context.proceed();
        }
        BaseResource resource = (BaseResource) context.getTarget();
        try (Authentication ignored = auth(resource.getToken())) {
            return context.proceed();
        }
    }
}
