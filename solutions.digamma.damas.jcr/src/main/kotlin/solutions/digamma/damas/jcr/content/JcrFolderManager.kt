package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrPathFinder
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import javax.inject.Singleton
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR implementation convert folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
class JcrFolderManager :
        JcrCrudManager<Folder>(),
        JcrPathFinder<Folder>,
        JcrSearchEngine<Folder>,
        FolderManager {

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(
            session: Session,
            id: String): JcrFolder {
        return JcrFolder(session.getNodeByIdentifier(id))
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(
            session: Session,
            entity: Folder): JcrFolder {
        val name = entity.name
        val parent = session.getNodeByIdentifier(entity.parentId)
        val node = parent.addNode(name, TypeNamespace.FOLDER)
        node.addMixin(TypeNamespace.FILE)
        return JcrFolder(node)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(
            session: Session,
            id: String,
            entity: Folder): JcrFolder {
        val folder = this.retrieve(session, id)
        folder.update(entity)
        return folder
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) {
        val folder = this.retrieve(session, id)
        folder.remove()
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(
            session: Session,
            offset: Int,
            size: Int,
            query: Any?): Page<Folder> {
        val folder = JcrFolder(session.getNode(JcrFile.ROOT_PATH))
        val folders = ArrayList<Folder>(1)
        folders.add(folder)
        return ResultPage(folders, 0, 1)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(session: Session, path: String): Folder {
        return JcrFolder(
                session.getNode(JcrFile.ROOT_PATH).getNode(path))
    }
}
