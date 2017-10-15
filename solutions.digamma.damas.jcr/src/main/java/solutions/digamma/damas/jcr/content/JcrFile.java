package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.InternalStateException;
import solutions.digamma.damas.common.UnsupportedOperationException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.common.Exceptions;
import solutions.digamma.damas.jcr.model.JcrBaseEntity;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.net.URI;

/**
 * JCR-based implementation convert file abstract type.
 *
 * @author Ahmad Shahwan
 */
public abstract class JcrFile extends JcrBaseEntity
        implements File {

    /**
     * Content folder JCR path.
     */
    public static final String ROOT_PATH = "/content";

    /**
     * Constructor.
     *
     * @param node
     */
    JcrFile(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    protected void checkCompatibility() throws InternalStateException {
        this.checkTypeCompatibility(TypeNamespace.FILE);
        try {
            if (!this.node.getPath().startsWith(ROOT_PATH)) {
                throw new InternalStateException(
                        "Node not in content root.");
            }
        } catch (RepositoryException e) {
            throw new InternalStateException(e);
        }
    }

    @Override
    public @NotNull String getName() throws WorkspaceException {
        try {
            return this.node.getName();
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    @Override
    public void setName(@NotNull String value) throws WorkspaceException {
        try {
            /* Use node's path since paths don't end with a slash.
             */
            String destination = URI
                    .create(this.node.getPath())
                    .resolve(value)
                    .getPath();
            this.move(destination);
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    @Override
    public @Nullable Folder getParent() throws WorkspaceException {
        Node parent;
        try {
            parent = this.node.getParent();
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
        if (parent == null) {
            return NO_PARENT;
        }
        return new JcrFolder(parent);
    }

    @Override
    public void setParent(@NotNull Folder value) throws WorkspaceException {
        String id = value.getId();
        if (id == null) {
            throw new UnsupportedOperationException("Parent ID is null.");
        }
        this.setParentId(id);
    }

    @Override
    public String getParentId() throws WorkspaceException {
        try {
            return this.node.getParent().getIdentifier();
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    @Override
    public void setParentId(@NotNull String value) throws WorkspaceException {
        try {
            String path = this.getSession()
                    .getNodeByIdentifier(value)
                    .getPath()
                    .concat("/");
            String destination = URI
                .create(path)
                .resolve(this.node.getName())
                .getPath();
            this.move(destination);
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
    }

    private void move(@NotNull String path) throws RepositoryException {
        this.node.getSession().move(this.node.getPath(), path);
    }
}
