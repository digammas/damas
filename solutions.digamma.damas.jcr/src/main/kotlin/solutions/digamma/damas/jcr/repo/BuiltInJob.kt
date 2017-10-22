package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.jcr.content.JcrFile
import java.util.*

/**
 * Built-in repository job.
 *
 * @author Ahmad Shahwan
 */
class BuiltInJob
/**
 * Constructor.
 */
private constructor() : RepositoryJob {

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    override val creations: List<RepositoryJob.Node>
        get() = Arrays.asList<RepositoryJob.Node>(*CREATIONS)

    /**
     * Node specification.
     */
    private class Node(
            override val path: String,
            override val type: String,
            override val mixins: List<String>) : RepositoryJob.Node
    companion object {

        val INSTANCE = BuiltInJob()

        private val CREATIONS = arrayOf(Node(
                JcrFile.ROOT_PATH,
                TypeNamespace.FOLDER,
                Collections.emptyList()
        ))
    }
}
