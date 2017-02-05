package solutions.digamma.damas.core;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import solutions.digamma.damas.api.DocumentException;
import solutions.digamma.damas.api.Entity;
import solutions.digamma.damas.api.inspection.Nonnull;

/**
 * Generic entity, implemented as a JCR node.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrEntity implements Entity {

    /**
     * JCR node.
     */
    @Nonnull protected final Node node;

    public JcrEntity(@Nonnull final Node node) throws DocumentException {
        this.node = node;
        checkCompatibility();
    }

    @Override
    public String getId() throws JcrException {
        try {
            return this.node.getIdentifier();
        } catch (RepositoryException e) {
            throw new JcrException("Exception while acquiring ID.", e);
        }
    }

    public @Nonnull Session getSession() throws JcrException {
        try {
            return this.node.getSession();
        } catch (RepositoryException e) {
            throw new JcrException(e);
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
