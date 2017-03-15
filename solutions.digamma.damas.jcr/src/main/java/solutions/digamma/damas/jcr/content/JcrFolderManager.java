package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.model.JcrFullManager;

import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.ArrayList;
import java.util.List;

/**
 * JCR implementation of folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrFolderManager
        extends JcrFullManager<Folder> implements FolderManager {

    @Override
    protected JcrFolder retrieve(
            @Nonnull Session session,
            @Nonnull String id)
            throws RepositoryException, DocumentException {
        return new JcrFolder(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrFolder create(
            @Nonnull Session session,
            @Nonnull Folder entity)
            throws RepositoryException, DocumentException {
        String parentId = entity.getParentId();
        String name = entity.getName();
        Node parent = session.getNodeByIdentifier(parentId);
        Node node = parent.addNode(NodeType.NT_FOLDER, name);
        node.addMixin(Namespace.FILE);
        return new JcrFolder(node);
    }

    @Override
    protected JcrFolder update(
            @Nonnull Session session,
            @Nonnull String id,
            @Nonnull Folder entity)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.update(entity);
        return folder;
    }

    @Override
    protected void delete(@Nonnull Session session, @Nonnull String id)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.remove();
    }

    @Override
    protected Page<Folder> find(
            @Nonnull Session session,
            int offset,
            int size,
            @Nullable Object query)
            throws RepositoryException, DocumentException {
        Folder folder = new JcrFolder(session.getNode(JcrFile.CONTENT_ROOT));
        List<Folder> folders = new ArrayList<>(1);
        folders.add(folder);
        return new ContentPage<>(folders, 0, 1);
    }
}
