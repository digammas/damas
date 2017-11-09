package solutions.digamma.damas.jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

public abstract class PasswordBasedLoginModule implements LoginModule {
    protected Subject subject;
    private CallbackHandler callbackHandler;
    protected String login;
    protected char[] password;
    protected Map sharedState;

    private static final String USERNAME = "javax.security.auth.login.name";
    private static final String PASSWORD = "javax.security.auth.login.password";

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.extractCredentials();
    }

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
