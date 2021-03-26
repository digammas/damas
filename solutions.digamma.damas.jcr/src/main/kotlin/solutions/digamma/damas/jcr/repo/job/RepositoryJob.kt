package solutions.digamma.damas.jcr.repo.job

/**
 * Repository job.
 *
 * Repository jobs are specification of node types and structures that are used
 * for repository initialization during bootstrap.
 *
 * @author Ahmad Shahwan
 */
interface RepositoryJob {

    companion object {
        /**
         * Configuration key that, when set to true, allows the whole
         * initialization to be skipped.
         *
         * This can be useful for optimizing start-up time.
         */
        const val SKIP_INIT_ALL_CONF_KEY    = "damas.init.skipAll"

        /**
         * Configuration key that, when set to true, allows namespace
         * initialization to be skipped.
         *
         * This can be useful to speed up bootstrap, specially when coupled with
         * skipping node type initialization.
         */
        const val SKIP_INIT_NS_CONF_KEY     = "damas.init.skipNamespaces"

        /**
         * Configuration key that, when set to true, allows node type
         * initialization to be skipped.
         *
         * This can be useful to speed up bootstrap, specially when coupled with
         * skipping namespace initialization.
         */
        const val SKIP_INIT_NT_CONF_KEY     = "damas.init.skipNodeTypes"

        /**
         * Configuration key that, when set to true, allows node structure
         * initialization to be skipped.
         *
         * This can be useful to speed up bootstrap.
         */
        const val SKIP_INIT_CREATE_CONF_KEY = "damas.init.skipCreation"
    }

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
