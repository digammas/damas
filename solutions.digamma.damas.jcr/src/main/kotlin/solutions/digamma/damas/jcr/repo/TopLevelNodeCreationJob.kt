package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.auth.JaasConfiguration
import solutions.digamma.damas.jcr.content.JcrFile
import solutions.digamma.damas.jcr.names.TypeNamespace
import solutions.digamma.damas.jcr.repo.job.NodeCreation
import solutions.digamma.damas.jcr.repo.job.RepositoryJob
import solutions.digamma.damas.jcr.sys.SystemRole
import solutions.digamma.damas.jcr.user.JcrSubject
import java.security.Principal
import javax.inject.Singleton
import javax.jcr.nodetype.NodeType
import javax.jcr.security.Privilege

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
        NodeCreation(
            path = ".",
            type = NodeType.NT_BASE,
            accessRights = mapOf(
                SystemRole.READWRITE.principalName to listOf(Privilege.JCR_ALL),
            ),
        ),
        NodeCreation(
            path = JcrFile.ROOT_PATH,
            type = TypeNamespace.FOLDER,
        ),
        NodeCreation(JcrSubject.ROOT_PATH, TypeNamespace.DIRECTORY)
    )
}
