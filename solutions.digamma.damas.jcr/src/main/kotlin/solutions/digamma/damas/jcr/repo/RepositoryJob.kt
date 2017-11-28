package solutions.digamma.damas.jcr.repo

/**
 * Repository job. Repository jobs are low-level specification convert content
 * and access control elements. They are used for repository initialization.
 *
 * @author Ahmad Shahwan
 */
internal interface RepositoryJob {

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    val creations: List<Node>

    /**
     * Node specification.
     */
    interface Node {

        val path: String

        val type: String

        val mixins: List<String>
    }
}
