package solutions.digamma.damas.jcr.repo.job

/**
 * Node creation specification.
 */
class NodeCreation(
    val path: String,
    val type: String,
    val mixins: List<String> = emptyList(),
    val accessRights: Map<String, List<String>> = emptyMap()
)
