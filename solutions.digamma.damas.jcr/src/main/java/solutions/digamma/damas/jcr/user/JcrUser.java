package solutions.digamma.damas.jcr.user;

import solutions.digamma.damas.common.MisuseException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.names.ItemNamespace;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.user.User;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;
import java.util.regex.Pattern;

/**
 * JCR-node-backed user implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrUser extends JcrSubject implements User {

    public static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    /**
     * Constructor with JCR-node.
     *
     * @param node JCR node
     * @throws WorkspaceException
     */
    protected JcrUser(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    public String getLogin() throws WorkspaceException {
        return Exceptions.wrap(this.node::getName);
    }

    @Override
    public void setPassword(String value) throws WorkspaceException {

    }

    @Override
    public String getEmailAddress() throws WorkspaceException {
        return getString(ItemNamespace.EMAIL);
    }

    @Override
    public void setEmailAddress(String value) throws WorkspaceException {
        validateEmailAddress(value);
        setString(ItemNamespace.EMAIL, value);
    }

    @Override
    public String getFirstName() throws WorkspaceException {
        return getString(ItemNamespace.FIRST_NAME);
    }

    @Override
    public void setFirstName(String value) throws WorkspaceException {
        setString(ItemNamespace.FIRST_NAME, value);
    }

    @Override
    public String getLastName() throws WorkspaceException {
        return getString(ItemNamespace.LAST_NAME);
    }

    @Override
    public void setLastName(String value) throws WorkspaceException {
        setString(ItemNamespace.LAST_NAME, value);
    }

    @Override
    public List<String> getMemberships() throws WorkspaceException {
        return this.getStrings(ItemNamespace.GROUPS);
    }

    private void validateEmailAddress(String value) throws MisuseException {
        if (value == null || !EMAIL_ADDRESS_REGEX.matcher(value).find()) {
            throw new MisuseException("Invalid email address.");
        }
    }

    public static JcrUser of(@NotNull Node node) throws WorkspaceException {
        return new JcrUser(node);
    }

    public static JcrUser from(@NotNull Session session, String login)
            throws WorkspaceException {
        try {
            Node root = session.getNode(ROOT_PATH);
            return of(root.addNode(login, TypeNamespace.USER));
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }
}
