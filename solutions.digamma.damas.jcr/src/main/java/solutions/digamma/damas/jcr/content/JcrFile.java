package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.UnsupportedOperationException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.error.IncompatiblePathException;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;

/**
 * JCR-based implementation of file abstract type.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrFile extends JcrBaseEntity
        implements File {

    /**
     * Content folder JCR path.
     */
    public static final String CONTENT_ROOT = "/content";

    /**
     * Constructor.
     *
     * @param node
     */
    public JcrFile(@NotNull Node node) throws DocumentException {
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
    public @NotNull String getName() throws DocumentException {
        try {
            return this.node.getName();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public void setName(@NotNull String value) throws DocumentException {
        try {
            String destination = URI
                    .create(this.node.getParent().getPath())
                    .resolve(value)
                    .getPath();
            this.move(destination);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @XmlTransient
    @Override
    public @Nullable Folder getParent() throws DocumentException {
        Node parent;
        try {
            parent = this.node.getParent();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
        if (parent == null) {
            return NO_PARENT;
        }
        return new JcrFolder(parent);
    }

    @Override
    public void setParent(@NotNull Folder value) throws DocumentException {
        try {
            String id = value.getId();
            if (id == null) {
                throw new UnsupportedOperationException("Parent ID is null.");
            }
            String path = this.getSession().getNodeByIdentifier(id).getPath();
            String destination = URI
                    .create(path)
                    .resolve(this.node.getName())
                    .getPath();
            this.move(destination);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public String getParentId() throws DocumentException {
        try {
            return this.node.getParent().getIdentifier();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    public void setParentId(@NotNull String value) throws DocumentException {
        try {
            String path = this.getSession().getNodeByIdentifier(value)
                    .getPath();
            String destination = URI
                .create(path)
                .resolve(this.node.getName())
                .getPath();
            this.move(destination);
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    public void remove() throws DocumentException {
        try {
            this.getNode().remove();
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    protected void move(@NotNull String path) throws RepositoryException {
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
            @NotNull String name,
            @NotNull String nodeType,
            @NotNull Node parent)
        throws DocumentException {
        try {
            Node node = parent.addNode(name, nodeType);
            node.addMixin(Namespace.FILE);
            return node;
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }
}
