package solutions.digamma.damas.jaas;

import java.io.Serializable;
import java.security.Principal;

/**
 * Simple {@link Principal} implementation.
 *
 * @author Ahmad Shahwan
 */
public class NamedPrincipal implements Principal, Serializable {

    private final String name;

    /**
     * Constructor.
     *
     * @param name      principal's name
     */
    public NamedPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
