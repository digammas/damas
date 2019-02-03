package solutions.digamma.damas.jcr.providers;

import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.security.AuthenticationProvider;
import org.modeshape.jcr.security.JaasProvider;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;
import javax.security.auth.login.LoginException;
import java.util.Map;

/**
 * Custom Modeshape security provider.
 *
 * This custom provider is necessary to workaround the use of deprecated class
 * <code>java.security.acl.Group</code> in Modeshape <code>
 * JaasSecurityContext</code>.
 *
 */
public class CustomSecurityProvider implements AuthenticationProvider {

    /**
     * Policy name.
     *
     * The policy name must match the realm name of the login module to be used.
     */
    private String policy;
    private JaasProvider delegate;

    /**
     * No argument constructor. This constructor is used by Modeshape to create
     * the instance.
     *
     */
    public CustomSecurityProvider() {
        this(null);
    }

    /**
     * Constructor that takes policy name as an argument.
     *
     * @param policy Policy name.
     */
    public CustomSecurityProvider(String policy) {
        this.policy = policy;
    }

    /**
     * Set the policy name.
     *
     * @param policy Policy name.
     */
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    /**
     * Get a delegate authentication provider, creating one if necessary.
     * When a provider is created, it with be configured with the current policy
     * name.
     *
     * @return A delegate authentication provider.
     * @throws LoginException
     */
    protected AuthenticationProvider getDelegate() throws LoginException {
        if (this.delegate == null) {
            this.delegate = new JaasProvider(this.policy);
        }
        return this.delegate;
    }

    @Override
    public ExecutionContext authenticate(
            Credentials credentials,
            String repository,
            String workspace,
            ExecutionContext context,
            Map<String, Object> attributes) {
        try {
            if (credentials instanceof SimpleCredentials) {
                SimpleCredentials simple = (SimpleCredentials) credentials;
                return context.with(new JaasSecurityContext(
                        policy, simple.getUserID(), simple.getPassword()));
            }
            return this.getDelegate().authenticate(
                    credentials, repository, workspace, context, attributes);
        } catch (LoginException e) {
            return null;
        }
    }
}
