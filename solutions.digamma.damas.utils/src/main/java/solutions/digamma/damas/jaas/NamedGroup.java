package solutions.digamma.damas.jaas;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Simple {@link Group} implementation.
 *
 * @author Ahmad Shahwan
 */
public class NamedGroup extends NamedPrincipal implements Group {

    public static final String DEFAULT_NAME = "Roles";

    private Vector<Principal> members;

    /**
     * No-args Constructor.
     */
    public NamedGroup() {
        this(DEFAULT_NAME);
    }

    /**
     * Constructor with name.
     *
     * @param name      principal's name
     */
    public NamedGroup(String name) {
        super(name);
        this.members = new Vector<>();
    }

    /**
     * Constructor with name.
     *
     * @param name      principal's name
     */
    public NamedGroup(String name, List<Principal> roles) {
        super(name);
        this.members = new Vector<>(roles);
    }

    @Override
    public boolean addMember(Principal principal) {
        return this.members.add(principal);
    }

    @Override
    public boolean removeMember(Principal principal) {
        return this.members.remove(principal);
    }

    @Override
    public boolean isMember(Principal principal) {
        return this.members.contains(principal);
    }

    @Override
    public Enumeration<? extends Principal> members() {
        return this.members.elements();
    }
}
