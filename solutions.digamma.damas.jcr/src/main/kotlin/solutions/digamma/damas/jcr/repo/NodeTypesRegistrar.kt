package solutions.digamma.damas.jcr.repo

import solutions.digamma.damas.jcr.repo.job.ChildNodeDeclaration
import solutions.digamma.damas.jcr.repo.job.ChildPropertyDeclaration
import solutions.digamma.damas.jcr.repo.job.NodeTypeDeclaration
import solutions.digamma.damas.logging.Logbook
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.ValueFactory
import javax.jcr.nodetype.NodeDefinitionTemplate
import javax.jcr.nodetype.NodeType
import javax.jcr.nodetype.NodeTypeManager
import javax.jcr.nodetype.NodeTypeTemplate
import javax.jcr.nodetype.PropertyDefinitionTemplate

@Singleton
class NodeTypesRegistrar {

    @Inject
    private lateinit var logger: Logbook

    fun registerNodeTypes(
        types: List<NodeTypeDeclaration>,
        session: Session) {
        try {
            val manager = session.workspace.nodeTypeManager

            val templates = types
                .associateBy { it.name!! }
            val toRegister = templates.values.filter {
                !manager.hasNodeType(it.name)
            }
            toRegister.forEach {
                ensureNtBase(it, templates, manager)
            }
            this.doRegister(toRegister, session)
        } catch (e: RepositoryException) {
            this.fail("Unable to acquire node type manager", e)
        }
    }

    @Throws(RepositoryException::class)
    private fun ensureNtBase(
        ntt: NodeTypeDeclaration, templates: Map<String, NodeTypeDeclaration>,
        manager: NodeTypeManager
    ) {
        if (ntt.mixin || NodeType.NT_BASE == ntt.name) {
            return
        }
        /* Check whether an implicit "nt:base" supertype is needed */
        val needsNtBase = ntt.types
            .map { templates[it]?.mixin ?: manager.getNodeType(it)?.isMixin }
            .all { it != false}
        if (needsNtBase) {
            ntt.types.add(NodeType.NT_BASE)
        }
    }

    @Throws(RepositoryException::class)
    private fun doRegister(defs: List<NodeTypeDeclaration>, session: Session) {
        val manager = session.workspace.nodeTypeManager
        val factory = session.valueFactory
        defs.forEach {
            this.registerTemplate(this.template(manager, factory , it), manager)
        }
    }

    private fun registerTemplate(
        template: NodeTypeTemplate, manager: NodeTypeManager) {
        try {
            manager.registerNodeType(template, true)
            this.logger.info { "Node type ${template.name} registered." }
        } catch (e: RepositoryException) {
            this.fail("Unable to register node type ${template.name}.", e)
        }
    }

    private fun template(
        manager: NodeTypeManager,
        factory: ValueFactory,
        def: NodeTypeDeclaration
    ): NodeTypeTemplate {
        val res = manager.createNodeTypeTemplate()
        res.name = def.name
        res.setDeclaredSuperTypeNames(def.types.toTypedArray())
        res.isAbstract = def.abstract
        res.isMixin = def.mixin
        res.setOrderableChildNodes(def.sortable)
        def.queryable?.let { res.isQueryable = it }
        res.propertyDefinitionTemplates.addAll(
            def.properties.map { this.template(manager, factory, it) }
        )
        res.nodeDefinitionTemplates.addAll(
            def.nodes.map { this.template(manager, it) }
        )
        res.primaryItemName =
            def.primaryItem ?:
            def.properties.firstOrNull { it.primary }?.name ?:
            def.nodes.firstOrNull { it.primary }?.name
        return res
    }

    private fun template(
        manager: NodeTypeManager,
        factory: ValueFactory,
        def: ChildPropertyDeclaration,
    ): PropertyDefinitionTemplate {
        val res = manager.createPropertyDefinitionTemplate()
        res.name = def.name
        res.requiredType = def.type!!
        res.defaultValues = def.defaultValues
            .map { factory.createValue(it.toString(), def.type!!) }
            .toTypedArray()
        res.valueConstraints = def.constraints.toTypedArray()
        res.isMandatory = def.mandatory
        res.isAutoCreated = def.autocreated
        res.isProtected = def.protected
        res.isMultiple = def.multiple
        res.isFullTextSearchable = def.searchable
        res.isQueryOrderable = def.queryOrder
        def.queryOperators?.let {
            res.availableQueryOperators = it.toTypedArray()
        }
        res.onParentVersion = def.version
        return res
    }

    private fun template(
        manager: NodeTypeManager,
        def: ChildNodeDeclaration
    ): NodeDefinitionTemplate {
        val res = manager.createNodeDefinitionTemplate()
        res.name = def.name
        res.defaultPrimaryTypeName = def.types.getOrNull(0)
        res.requiredPrimaryTypeNames = def.types.toTypedArray()
        res.isMandatory = def.mandatory
        res.isAutoCreated = def.autocreated
        res.isProtected = def.protected
        res.setSameNameSiblings(def.multiple)
        res.onParentVersion = def.version
        return res
    }

    private fun fail(message: String, ex: RepositoryException): Nothing {
        throw IllegalStateException(message, ex)
    }
}
