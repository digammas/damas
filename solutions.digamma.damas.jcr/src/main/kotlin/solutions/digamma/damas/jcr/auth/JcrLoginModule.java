package solutions.digamma.damas.jcr.auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author Ahmad Shahwan
 */
public class JcrLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private String login;
    private char[] password;
    private Map<String, ?> sharedState;
    private List<String> roles;

    private static final String USERNAME = "javax.security.auth.login.name";
    private static final String PASSWORD = "javax.security.auth.login.password";

    @Override
    public void initialize(
            Subject subject,
            CallbackHandler callbackHandler,
            Map<String, ?> sharedState,
            Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.extractCredentials();
    }

    @Override
    public boolean login() throws LoginException {
        if (this.login == null || this.password == null) {
            throw new LoginException("Missing login");
        }
        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        return false;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }

    private void extractCredentials() {
        Object userObj = sharedState.get(USERNAME);
        Object passObj = sharedState.get(PASSWORD);
        if (userObj instanceof Principal)
            this.login = ((Principal) userObj).getName();
        else if (userObj != null) {
            this.login = userObj.toString();
        }
        if (passObj instanceof char[] )
            this.password = (char[]) passObj;
        else if (passObj != null ) {
            this.password = passObj.toString().toCharArray();
        }
        if (this.login != null && this.password != null) {
            return;
        }
        NameCallback nameCallback = new NameCallback("username");
        PasswordCallback passwordCallback =
                new PasswordCallback("password", false);
        try {
            this.callbackHandler.handle(new Callback[] {
                    nameCallback, passwordCallback
            });
            this.login = nameCallback.getName();
            this.password = passwordCallback.getPassword();
        } catch (IOException | UnsupportedCallbackException e) {
            this.login = null;
            this.password = null;
        }
    }
}
