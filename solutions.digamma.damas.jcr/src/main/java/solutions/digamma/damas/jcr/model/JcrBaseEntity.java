package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.InternalStateException;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrException;

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

    public JcrBaseEntity(@NotNull final Node node) throws DocumentException {
        this.node = node;
        checkCompatibility();
    }

    public @NotNull Session getSession() throws DocumentException {
        try {
            return this.node.getSession();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
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
     * @throws DocumentException
     */
    public void remove() throws DocumentException {
        try {
            this.getNode().remove();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    /**
     * Check if JCR node of a particular node type. Throws
     * {@link InternalStateException} if not.
     *
     * @param typeName                  type name
     * @throws InternalStateException   thrown when node is not of type
     */
    protected void checkTypeCompatibility(String typeName)
        throws InternalStateException {
        try {
            if (!this.node.isNodeType(typeName)) {
                String message = String.format(
                        "Node is not of %s type.", typeName);
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
