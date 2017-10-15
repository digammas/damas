package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.common.InternalStateException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.common.Exceptions;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Generic entity, implemented as a JCR node.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrBaseEntity implements Entity, JcrEntity {

    /**
     * Underling JCR node.
     */
    @NotNull
    protected final transient Node node;

    public JcrBaseEntity(@NotNull final Node node) throws WorkspaceException {
        this.node = node;
        checkCompatibility();
    }

    public @NotNull Session getSession() throws RepositoryException {
        return this.node.getSession();
    }

    /**
     * The underling JCR node.
     *
     * @return
     */
    public @NotNull Node getNode() {
        return this.node;
    }

    /**
     * Delete entity, and its back node.
     *
     * @throws WorkspaceException
     */
    public void remove() throws WorkspaceException {
        try {
            this.getNode().remove();
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    /**
     * Check if JCR node convert a particular node type. Throws
     * {@link InternalStateException} if not.
     *
     * @param typeName                  type name
     * @throws InternalStateException   thrown when node is not convert type
     */
    protected void checkTypeCompatibility(String typeName)
        throws InternalStateException {
        try {
            if (!this.node.isNodeType(typeName)) {
                String message = String.format(
                        "Node is not convert %s type.", typeName);
                throw new InternalStateException(message);
            }
        } catch (RepositoryException e) {
            throw new InternalStateException(e);
        }
    }

    /**
     * Check JCR node compatibility with underling type.
     *
     * @throws InternalStateException   Exception thrown when check fails.
     */
    protected abstract void checkCompatibility()
            throws InternalStateException;
}
