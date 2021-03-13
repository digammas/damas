package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.common.MisuseException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.content.PathFinder
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.session.JcrSessionConsumer
import java.nio.file.Paths
import javax.jcr.RepositoryException

/**
 * An interface that provide path finding functionality.
 *
 * @author Ahmad Shahwan
 */
internal interface JcrPathFinder<T : Entity> : JcrSessionConsumer, PathFinder<T> {

    /**
     * This method converts a thrown exception into a [WorkspaceException].
     * It also normalizes the path to a JCR compatible one.
     */
    @Throws(WorkspaceException::class)
    override fun find(path: String): T = Exceptions.check {
        var p = Paths.get(path).normalize().toString()
        if (p.startsWith("../")) {
            throw MisuseException("Invalid relative path.")
        }
        if (p == "") p = "."
        this.doFind(p)
    }

    /**
     * Perform path look-up.
     *
     * @param path
     * @return
     * @throws RepositoryException
     * @throws WorkspaceException
     */
    @Throws(RepositoryException::class, WorkspaceException::class)
    fun doFind(path: String): T
}
