package solutions.digamma.damas.core.content;

import solutions.digamma.damas.api.DocumentException;
import solutions.digamma.damas.api.UnsupportedOperationException;
import solutions.digamma.damas.api.content.Comment;
import solutions.digamma.damas.api.content.File;
import solutions.digamma.damas.api.content.Folder;
import solutions.digamma.damas.api.inspection.Nonnull;
import solutions.digamma.damas.api.inspection.Nullable;
import solutions.digamma.damas.core.*;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * JCR-based implementation of file abstract type.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrFile extends JcrEntity implements File {

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
    public Comment[] getComments() {
        return new Comment[0];
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
            String destination = new JcrPath(this.node.getParent().getPath())
                    .append(value)
                    .toString();
            this.move(destination);
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    @Override
    public @Nullable Folder getParent() throws DocumentException {
        Node parent = null;
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
            String destination = new JcrPath(value.getPath())
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
            String destination = new JcrPath(path)
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
            return new JcrPath(this.node.getPath()).trim(CONTENT_ROOT).toString();
        } catch (RepositoryException e) {
            throw new JcrException(e);
        }
    }

    protected void move(@Nonnull String path) throws RepositoryException {
        this.node.getSession().move(this.node.getPath(), path);
    }
}
