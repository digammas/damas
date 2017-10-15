package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.common.ResultPage;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.model.JcrPathFinder;
import solutions.digamma.damas.jcr.model.JcrSearchEngine;

import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * JCR implementation convert folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrFolderManager
        extends JcrCrudManager<Folder>
        implements JcrPathFinder<Folder>,
            JcrSearchEngine<Folder>,
            FolderManager {

    @Override
    protected JcrFolder retrieve(
            @NotNull Session session,
            @NotNull String id)
            throws RepositoryException, WorkspaceException {
        return new JcrFolder(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrFolder create(
            @NotNull Session session,
            @NotNull Folder entity)
            throws RepositoryException, WorkspaceException {
        String name = entity.getName();
        Node parent = session.getNodeByIdentifier(entity.getParentId());
        Node node = parent.addNode(name, TypeNamespace.FOLDER);
        node.addMixin(TypeNamespace.FILE);
        return new JcrFolder(node);
    }

    @Override
    protected JcrFolder update(
            @NotNull Session session,
            @NotNull String id,
            @NotNull Folder entity)
            throws RepositoryException, WorkspaceException {
        JcrFolder folder = this.retrieve(session, id);
        folder.update(entity);
        return folder;
    }

    @Override
    protected void delete(@NotNull Session session, @NotNull String id)
            throws RepositoryException, WorkspaceException {
        JcrFolder folder = this.retrieve(session, id);
        folder.remove();
    }

    @Override
    public Page<Folder> find(
            @NotNull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, WorkspaceException {
        Folder folder = new JcrFolder(session.getNode(JcrFile.ROOT_PATH));
        List<Folder> folders = new ArrayList<>(1);
        folders.add(folder);
        return new ResultPage<>(folders, 0, 1);
    }

    @Override
    public Folder find(Session session, String path)
            throws RepositoryException, WorkspaceException {
        return new JcrFolder(
                session.getNode(JcrFile.ROOT_PATH).getNode(path));
    }
}
