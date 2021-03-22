package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.jcr.repo.job.NodeCreation
import solutions.digamma.damas.jcr.repo.job.RepositoryJob
import solutions.digamma.damas.jcr.user.JcrSubject
import javax.inject.Singleton

/**
 * Built-in repository job.
 *
 * @author Ahmad Shahwan
 */
@Singleton
internal object TopLevelNodeCreationJob : RepositoryJob {

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    override val nodes: List<NodeCreation> = listOf(
        NodeCreation(JcrFile.ROOT_PATH, TypeNamespace.FOLDER),
        NodeCreation(JcrSubject.ROOT_PATH, TypeNamespace.DIRECTORY)
    )
}
