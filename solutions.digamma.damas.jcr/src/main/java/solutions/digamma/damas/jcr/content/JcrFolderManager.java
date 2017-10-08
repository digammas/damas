package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.Namespace;
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
 * JCR implementation of folder manager.
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
            throws RepositoryException, DocumentException {
        return new JcrFolder(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrFolder create(
            @NotNull Session session,
            @NotNull Folder entity)
            throws RepositoryException, DocumentException {
        String name = entity.getName();
        Node parent = session.getNodeByIdentifier(entity.getParentId());
        Node node = parent.addNode(name, Namespace.FOLDER);
        node.addMixin(Namespace.FILE);
        return new JcrFolder(node);
    }

    @Override
    protected JcrFolder update(
            @NotNull Session session,
            @NotNull String id,
            @NotNull Folder entity)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.update(entity);
        return folder;
    }

    @Override
    protected void delete(@NotNull Session session, @NotNull String id)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.remove();
    }

    @Override
    public Page<Folder> find(
            @NotNull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, DocumentException {
        Folder folder = new JcrFolder(session.getNode(JcrFile.CONTENT_ROOT));
        List<Folder> folders = new ArrayList<>(1);
        folders.add(folder);
        return new ContentPage<>(folders, 0, 1);
    }

    @Override
    public Folder find(Session session, String path)
            throws RepositoryException, DocumentException {
        /**
         * TODO: Solve for path-hacks. Ex: path = "../secret/place".
         */
        return new JcrFolder(
                session.getNode(JcrFile.CONTENT_ROOT).getNode(path));
    }
}
