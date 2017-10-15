package solutions.digamma.damas.jcr.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.user.User;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;
import java.util.UUID;

/**
 * JCR-node-backed user implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrUser extends JcrSubject implements User {

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
        return null;
    }

    @Override
    public void setPassword(String value) throws WorkspaceException {

    }

    @Override
    public String getEmailAddress() throws WorkspaceException {
        return null;
    }

    @Override
    public void setEmailAddress(String value) throws WorkspaceException {

    }

    @Override
    public List<String> getMemberships() throws WorkspaceException {
        return null;
    }

    @Override
    public @Nullable String getId() throws WorkspaceException {
        return null;
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
