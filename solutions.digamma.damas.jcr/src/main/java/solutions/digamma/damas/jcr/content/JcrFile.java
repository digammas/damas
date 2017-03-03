package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.UnsupportedOperationException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.Metadata;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.fail.IncompatiblePathException;
import solutions.digamma.damas.jcr.fail.JcrException;
import solutions.digamma.damas.jcr.Path;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;
import solutions.digamma.damas.jcr.model.JcrCreated;
import solutions.digamma.damas.jcr.model.JcrModifiable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * JCR-based implementation of file abstract type.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrFile extends JcrBaseEntity
        implements File, JcrCreated, JcrModifiable, JcrCommentReceiver {

    /**
     * Content folder JCR path.
     */
    public static final String CONTENT_ROOT = "/content";

    /**
     * Constructor.
     *
     * @param node
     */
    public JcrFile(@Nonnull Node node) throws DocumentException {
        super(node);
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        this.checkTypeCompatibility(Namespace.FILE);
        try {
            if (!this.node.getPath().startsWith(CONTENT_ROOT)) {
                throw new IncompatiblePathException(
                        "Node not in content root.");
            }
        } catch (RepositoryException e) {
            throw new CompatibilityException(e);
        }
    }

    @Override
    public @Nonnull String getName() throws JcrException {
        try {
            return this.node.getName();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public void setName(@Nonnull String value) throws DocumentException {
        try {
            String destination = new Path(this.node.getParent().getPath())
                    .append(value)
                    .toString();
            this.move(destination);
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public @Nullable Folder getParent() throws DocumentException {
        Node parent;
        try {
            parent = this.node.getParent();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
        if (parent == null) {
            return NO_PARENT;
        }
        return new JcrFolder(parent);
    }

    @Override
    public void setParentId(@Nonnull Folder value) throws DocumentException {
        try {
            if (value.getPath() == null) {
                throw new UnsupportedOperationException("Parent path is null.");
            }
            String destination = new Path(value.getPath())
                    .append(this.node.getName())
                    .toString();
            this.move(destination);
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public String getParentId() throws JcrException {
        try {
            return this.node.getParent().getIdentifier();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public void setParentId(@Nonnull String value) throws DocumentException {
        try {
            String path = this.getSession().getNodeByIdentifier(value)
                    .getPath();
            String destination = new Path(path)
                    .append(this.node.getName())
                    .toString();
            this.move(destination);
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public @Nonnull String getPath() throws DocumentException {
        try {
            return new Path(this.node.getPath()).trim(CONTENT_ROOT).toString();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public @Nullable Metadata getMetadata() throws DocumentException {
        return null;
    }

    @Override
    public void setMetadata(@Nullable Metadata metadata) throws DocumentException {

    }

    public void remove() throws DocumentException {
        try {
            this.getNode().remove();
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

    protected void move(@Nonnull String path) throws RepositoryException {
        this.node.getSession().move(this.node.getPath(), path);
    }
    
    /**
     * Help method to create objects.
     *
     * @param name
     * @param parent
     * @return
     * @throws DocumentException
     */
    static protected Node create(
            @Nonnull String name,
            @Nonnull String nodeType,
            @Nonnull Node parent)
        throws DocumentException {
        try {
            Node node = parent.addNode(name, nodeType);
            node.addMixin(Namespace.FILE);
            return node;
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }
}
