package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.auth.UserSession;
import solutions.digamma.damas.jcr.model.JcrCrudManager;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

/**
 * JCR implementation of folder manager.
 *
 * @author Ahmad Shahwan
 */
public class JcrFolderManager
        extends JcrCrudManager<Folder> implements FolderManager {

    @Override
    protected JcrFolder retrieve(
            @Nonnull UserSession session,
            @Nonnull String id)
            throws RepositoryException, DocumentException {
        return new JcrFolder(session.use().getNodeByIdentifier(id));
    }

    @Override
    public JcrFolder create(
            @Nonnull UserSession session,
            @Nonnull Folder entity)
            throws RepositoryException, DocumentException {
        String parentId = entity.getParentId();
        String name = entity.getName();
        Node parent = session.use().getNodeByIdentifier(parentId);
        Node node = parent.addNode(NodeType.NT_FOLDER, name);
        node.addMixin(Namespace.FILE);
        return new JcrFolder(node);
    }

    @Override
    public JcrFolder update(
            @Nonnull UserSession session,
            @Nonnull String id,
            @Nonnull Folder entity)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.update(entity);
        return folder;
    }

    @Override
    public void delete(@Nonnull UserSession session, @Nonnull String id)
            throws RepositoryException, DocumentException {
        JcrFolder folder = this.retrieve(session, id);
        folder.remove();
    }

}
