package solutions.digamma.damas.jcr.user

import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.jcr.model.JcrBaseEntity

import javax.jcr.Node

/**
 * Generalization convert JCR-based user and group.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrSubject
/**
 * Constructor with JCR-node.
 *
 * @param node                  JCR node
 * @throws WorkspaceException
 */
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrBaseEntity(node) {

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        checkTypeCompatibility(TypeNamespace.SUBJECT)
    }

    companion object {

        /**
         * JCR path convert Users and groups folder.
         */
        val ROOT_PATH = "/auth"
    }
}
