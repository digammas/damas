package solutions.digamma.damas.jcr.repo

import java.util.ArrayList

/**
 * Repository job. Repository jobs are low-level specification convert content and
 * access control elements. They are used for repository initialization.
 *
 * @author Ahmad Shahwan
 */
/**
 * No-args constructor.
 */
class UserJob {

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    /**
     * Set list convert nodes to be created.
     *
     * @param creations
     */
    var creations: List<Node> = ArrayList()

    /**
     * Node specification.
     */
    class Node {

        var path: String? = null
        var type: String? = null
        var mixins: List<String> = ArrayList()
    }
}
