package solutions.digamma.damas.jcr.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;

import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.fail.IncompatibleNodeTypeException;
import solutions.digamma.damas.jcr.fail.JcrException;

/**
 * Generic entity, implemented as a JCR node.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrBaseEntity implements Entity, JcrEntity {

    /**
     * Underling JCR node.
     */
    @Nonnull protected final Node node;

    public JcrBaseEntity(@Nonnull final Node node) throws DocumentException {
        this.node = node;
        checkCompatibility();
    }

    public @Nonnull Session getSession() throws JcrException {
        try {
            return this.node.getSession();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    /**
     * The underling JCR node.
     *
     * @return
     */
    public  @Nonnull Node getNode() {
        return this.node;
    }

    protected void checkTypeCompatibility(String typeName)
        throws CompatibilityException {
        try {
            if (!this.node.isNodeType(NodeType.NT_FOLDER)) {
                String message = String.format(
                        "Node is not of %s type.", typeName);
                throw new IncompatibleNodeTypeException(message);
            }
        } catch (RepositoryException e) {
            throw new CompatibilityException(e);
        }
    }

    /**
     * Check JCR node compatibility with underling type.
     *
     * @throws CompatibilityException   Exception thrown when check fails.
     */
    protected abstract void checkCompatibility()
            throws CompatibilityException;

}
