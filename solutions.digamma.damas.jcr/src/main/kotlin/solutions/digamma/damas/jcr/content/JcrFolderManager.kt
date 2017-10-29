package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.entity.Page
import solutions.digamma.damas.jcr.common.ResultPage
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrPathFinder
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import java.util.Collections
import javax.inject.Singleton
import javax.jcr.RepositoryException
import javax.jcr.Session

/**
 * JCR implementation convert folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal class JcrFolderManager :
        JcrCrudManager<Folder>(),
        JcrPathFinder<Folder>,
        JcrSearchEngine<Folder>,
        FolderManager {

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun retrieve(session: Session, id: String) =
        JcrFolder.of(session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun create(
            session: Session,
            pattern: Folder) =
        JcrFolder.from(session, pattern.parentId, pattern.name)

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun update(session: Session, id: String, pattern: Folder) =
        this.retrieve(session, id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun delete(session: Session, id: String) =
        this.retrieve(session, id).remove()

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(
            session: Session,
            offset: Int,
            size: Int,
            query: Any?): Page<Folder> = ResultPage(Collections.singletonList(
                JcrFolder.of(session.getNode(JcrFile.ROOT_PATH))))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun find(session: Session, path: String) =
        JcrFolder.of(session.getNode(JcrFile.ROOT_PATH).getNode(path))
}
