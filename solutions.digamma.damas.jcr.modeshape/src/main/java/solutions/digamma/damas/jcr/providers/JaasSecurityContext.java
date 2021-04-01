package solutions.digamma.damas.jcr.providers;

import org.modeshape.jcr.security.JaasSecurityContext.UserPasswordCallbackHandler;
import org.modeshape.jcr.security.SecurityContext;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom security context.
 *
 * Unlike default JAAS security context provided by Modeshape, this class
 * doesn't make use of deprecated java packages.
 */
public class JaasSecurityContext implements SecurityContext {

    private final LoginContext context;
    private final String username;
    private Set<String> entitlements;
    private boolean logged;

    /**
     * Constructor.
     *
     * @param realmName Realm name of the login module.
     * @param username  Username.
     * @param password  Password.
     * @throws LoginException
     */
    public JaasSecurityContext(
            String realmName,
            String username,
            char[] password)
            throws LoginException {
        this.context = new LoginContext(
                realmName, new UserPasswordCallbackHandler(username, password));
        this.username = username;
        if (this.context.getSubject() == null) this.context.login();
        initialize(context.getSubject());
        this.logged = true;
    }

    private void initialize(Subject subject) {
        this.entitlements = subject != null ? subject
                .getPrincipals()
                .stream()
                .map(Principal::getName)
                .collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public String getUserName() {
        return logged ? username : null;
    }

    @Override
    public boolean hasRole( String roleName ) {
        return logged && entitlements.contains(roleName);
    }

    @Override
    public void logout() {
        try {
            logged = false;
            if (context != null) context.logout();
        } catch (LoginException e) {
            throw new IllegalStateException(e);
        }
    }
}

