package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.names.TypeNamespace
import java.util.Collections

/**
 * Built-in repository job.
 *
 * @author Ahmad Shahwan
 */
object BuiltInJob : RepositoryJob {

    /**
     * Node specification.
     */
    private class Node(
            override val path: String,
            override val type: String,
            override val mixins: List<String>) : RepositoryJob.Node

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    override val creations: List<RepositoryJob.Node> = listOf(
        Node(JcrFile.ROOT_PATH, TypeNamespace.FOLDER, Collections.emptyList())
    )
}
