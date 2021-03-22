package solutions.digamma.damas.jcr.cnd

import java.io.IOException
import java.io.Reader
import javax.inject.Singleton
import javax.jcr.NamespaceRegistry
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.UnsupportedRepositoryOperationException
import javax.jcr.ValueFactory
import javax.jcr.nodetype.InvalidNodeTypeDefinitionException
import javax.jcr.nodetype.NodeType
import javax.jcr.nodetype.NodeTypeExistsException
import javax.jcr.nodetype.NodeTypeManager
import javax.jcr.nodetype.NodeTypeTemplate

/**
 * Compact node type definitions importer.
 * @see CompactNodeTypeDefReader
 */
@Singleton
class CndImporter {

    @Throws(
        InvalidNodeTypeDefinitionException::class,
        NodeTypeExistsException::class,
        UnsupportedRepositoryOperationException::class,
        ParseException::class,
        RepositoryException::class,
        IOException::class
    )
    fun registerNodeTypes(cnd: Reader, session: Session): Array<NodeType> {
        val wsp = session.workspace
        return registerNodeTypes(
            cnd, wsp.nodeTypeManager, wsp.namespaceRegistry,
            session.valueFactory, false
        )
    }

    @Throws(
        ParseException::class,
        InvalidNodeTypeDefinitionException::class,
        NodeTypeExistsException::class,
        UnsupportedRepositoryOperationException::class,
        RepositoryException::class,
        IOException::class
    )
    fun registerNodeTypes(
        reader: Reader,
        nodeTypeManager: NodeTypeManager,
        namespaceRegistry: NamespaceRegistry,
        valueFactory: ValueFactory,
        reregisterExisting: Boolean
    ): Array<NodeType> {
        return reader.use { cnd ->
            val factory = TemplateBuilderFactory(
                nodeTypeManager,
                valueFactory,
                namespaceRegistry)
            val cndReader = CompactNodeTypeDefReader(cnd, factory)
            val templates = cndReader.nodeTypeDefinitions
                .associateBy { it.name }
            val toRegister = templates.values
                .filter {
                reregisterExisting || !nodeTypeManager.hasNodeType(it.name)
            }
            toRegister.forEach {
                ensureNtBase(it, templates, nodeTypeManager)
            }
            val registered = nodeTypeManager.registerNodeTypes(
                toRegister.toTypedArray(), true
            )
            registered
                .asSequence()
                .filterIsInstance<NodeType>()
                .toList()
                .toTypedArray()
        }
    }

    @Throws(RepositoryException::class)
    private fun ensureNtBase(
        ntt: NodeTypeTemplate, templates: Map<String, NodeTypeTemplate>,
        nodeTypeManager: NodeTypeManager
    ) {
        if (ntt.isMixin || NodeType.NT_BASE == ntt.name) {
            return
        }
        val supertypes = ntt.declaredSupertypeNames
        /* Check whether an implicit "nt:base" supertype is needed */
        val needsNtBase = supertypes
            .map { templates[it] ?: nodeTypeManager.getNodeType(it) }
            .all { it == null || !it.isMixin}
        if (needsNtBase) {
            val withNtBase = arrayOf(NodeType.NT_BASE, *supertypes)
            ntt.setDeclaredSuperTypeNames(withNtBase)
        }
    }
}
