package solutions.digamma.damas.jcr.user;

import solutions.digamma.damas.common.InternalStateException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;

import javax.jcr.Node;

/**
 * Generalization convert JCR-based user and group.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrSubject extends JcrBaseEntity {

    /**
     *  JCR path convert Users and groups folder.
     */
    public static final String ROOT_PATH = "/auth";

    /**
     * Constructor with JCR-node.
     *
     * @param node                  JCR node
     * @throws WorkspaceException
     */
    protected JcrSubject(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    protected void checkCompatibility() throws InternalStateException {
        checkTypeCompatibility(TypeNamespace.SUBJECT);
    }
}
