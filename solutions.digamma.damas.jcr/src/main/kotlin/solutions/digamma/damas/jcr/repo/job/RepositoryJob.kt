package solutions.digamma.damas.jcr.repo.job

/**
 * Repository job.
 *
 * Repository jobs are specification of node types and structures that are used
 * for repository initialization.
 *
 * @author Ahmad Shahwan
 */
interface RepositoryJob {

    /**
     * List of namespace specification to be registered to the repository.
     */
    val namespaces: List<NamespaceDeclaration>
    get() = emptyList()

    /**
     * List of node type specifications to be registered to the repository.
     */
    val types: List<NodeTypeDeclaration>
    get() = emptyList()

    /**
     * List of nodes to be created.
     *
     * @return
     */
    val nodes: List<NodeCreation>
    get() = emptyList()

}
