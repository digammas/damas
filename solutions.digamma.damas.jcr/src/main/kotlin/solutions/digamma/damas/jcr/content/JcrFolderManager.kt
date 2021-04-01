package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.Folder
import solutions.digamma.damas.content.FolderManager
import solutions.digamma.damas.jcr.model.JcrCrudManager
import solutions.digamma.damas.jcr.model.JcrPathFinder
import solutions.digamma.damas.jcr.model.JcrSearchEngine
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.logging.Logged
import javax.inject.Singleton
import javax.jcr.Node
import javax.jcr.RepositoryException

/**
 * JCR implementation convert folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal open class JcrFolderManager :
        JcrCrudManager<Folder>(),
        JcrPathFinder<Folder>,
        JcrSearchEngine<Folder>,
        FolderManager {

    @Logged
    @Throws(WorkspaceException::class)
    override fun copy(sourceId: String, destinationId: String): Folder {
        return this.doRetrieve(sourceId).duplicate(destinationId)
    }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doRetrieve(id: String) =
        JcrFolder.of(this.session.getNodeByIdentifier(id))

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doCreate(pattern: Folder) =
        JcrFolder.from(this.session, pattern.parentId, pattern.name)

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doUpdate(id: String, pattern: Folder) =
        this.doRetrieve(id).also { it.update(pattern) }

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doDelete(id: String) =
        this.doRetrieve(id).remove()

    @Throws(RepositoryException::class, WorkspaceException::class)
    override fun doFind(path: String) =
        JcrFolder.of(this.session.getNode("${JcrFile.ROOT_PATH}$path"))

    override fun getNodePrimaryType() = TypeNamespace.FOLDER

    override fun getDefaultRootPath() = JcrFile.ROOT_PATH

    override fun fromNode(node: Node) = JcrFolder.of(node)
}
