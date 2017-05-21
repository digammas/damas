package solutions.digamma.damas.jcr.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.error.IncompatibleNodeTypeException;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;

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
    protected final Node node;

    public JcrBaseEntity(@NotNull final Node node) throws DocumentException {
        this.node = node;
        checkCompatibility();
    }

    public @NotNull Session getSession() throws DocumentException {
        try {
            return this.node.getSession();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    /**
     * The underling JCR node.
     *
     * @return
     */
    public  @NotNull Node getNode() {
        return this.node;
    }

    protected void checkTypeCompatibility(String typeName)
        throws CompatibilityException {
        try {
            if (!this.node.isNodeType(typeName)) {
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
