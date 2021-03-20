package solutions.digamma.damas.auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A generic, username-and-password-based login module.
 *
 * This abstract class provide a default implementation to extract password-
 * based credentials. Implementation of {@code login()} method is left for sub-
 * classes.
 */
public abstract class AbstractLoginModule implements LoginModule {
    protected String login;
    protected char[] password;
    protected List<Principal> roles;
    private Subject subject;
    private boolean success;

    private Map<?, ?> sharedState;
    private CallbackHandler callbackHandler;

    private static final String USERNAME = "javax.security.login.login.name";
    private static final String PASSWORD = "javax.security.login.login.password";

    @Override
    public void initialize(
            Subject subject,
            CallbackHandler callbackHandler,
            Map sharedState,
            Map options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.extractCredentials();
        this.roles = new ArrayList<>();
    }

    @Override
    public boolean login() throws LoginException {
        return this.success = this.doLogin();
    }

    @Override
    public boolean commit() throws LoginException {
        if (!success) {
            return false;
        }
        Principal principal = () -> this.login;
        this.subject.getPrincipals().add(principal);
        this.subject.getPrincipals().addAll(this.roles);
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return this.success;
    }

    @Override
    public boolean logout() throws LoginException {
        this.subject.getPrincipals().clear();
        return true;
    }

    protected abstract boolean doLogin() throws LoginException;

    private void extractCredentials() {
        Object userObj = this.sharedState.get(USERNAME);
        Object passObj = this.sharedState.get(PASSWORD);
        if (userObj instanceof Principal) {
            this.login = ((Principal) userObj).getName();
        } else if (userObj != null) {
            this.login = userObj.toString();
        } else {
            NameCallback callback = new NameCallback("username");
            try {
                this.callbackHandler.handle(new NameCallback[] {callback});
                this.login = callback.getName();
            } catch (UnsupportedCallbackException | IOException e) {
                this.login = null;
            }
        }
        if (passObj instanceof char[]) {
            this.password = (char[]) passObj;
        } else if (passObj != null) {
            this.password = passObj.toString().toCharArray();
        } else {
            PasswordCallback callback = new PasswordCallback("password", false);
            try {
                this.callbackHandler.handle(new PasswordCallback[] {callback});
                this.password = callback.getPassword();
            } catch (UnsupportedCallbackException | IOException e) {
                this.password = null;
            }
        }
    }
}
