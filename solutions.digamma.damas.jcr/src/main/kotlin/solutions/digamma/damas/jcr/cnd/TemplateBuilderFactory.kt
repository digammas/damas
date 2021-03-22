package solutions.digamma.damas.jcr.cnd

import solutions.digamma.damas.jcr.cnd.TemplateBuilderFactory.NodeDefinitionTemplateBuilder
import solutions.digamma.damas.jcr.cnd.TemplateBuilderFactory.NodeTypeTemplateBuilder
import solutions.digamma.damas.jcr.cnd.TemplateBuilderFactory.PropertyDefinitionTemplateBuilder
import javax.jcr.NamespaceRegistry
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.UnsupportedRepositoryOperationException
import javax.jcr.Value
import javax.jcr.ValueFactory
import javax.jcr.ValueFormatException
import javax.jcr.nodetype.ConstraintViolationException
import javax.jcr.nodetype.NodeDefinitionTemplate
import javax.jcr.nodetype.NodeTypeManager
import javax.jcr.nodetype.NodeTypeTemplate
import javax.jcr.nodetype.PropertyDefinitionTemplate

/**
 * This implementation of [DefinitionBuilderFactory] can be used with
 * the [CompactNodeTypeDefReader] to produce node type definitions of type
 * [NodeTypeTemplate] and a namespace map of type [NamespaceRegistry].
 * It uses [NodeTypeTemplateBuilder] for building node type definitions,
 * [PropertyDefinitionTemplateBuilder] for building property definitions, and
 * [NodeDefinitionTemplateBuilder] for building node definitions.
 */
class TemplateBuilderFactory(
    private val nodeTypeManager: NodeTypeManager, private val valueFactory: ValueFactory,
    override var namespaceMapping: NamespaceRegistry
) : DefinitionBuilderFactory<NodeTypeTemplate, NamespaceRegistry>() {

    /**
     * Creates a new `TemplateBuilderFactory` for the specified
     * `Session`. This is equivalent to
     * [.TemplateBuilderFactory]
     * where all parameters are obtained from the given session object and
     * the workspace associated with it.
     *
     * @param session The repository session.
     * @throws RepositoryException If an error occurs.
     */
    constructor(session: Session) : this(
        session.workspace.nodeTypeManager,
        session.valueFactory,
        session.workspace.namespaceRegistry
    )

    @Throws(
        UnsupportedRepositoryOperationException::class,
        RepositoryException::class)
    override fun newNodeTypeDefinitionBuilder():
            AbstractNodeTypeDefinitionBuilder<NodeTypeTemplate> {
        return NodeTypeTemplateBuilder()
    }

    @Throws(RepositoryException::class)
    override fun setNamespace(prefix: String?, uri: String?) {
        namespaceMapping.registerNamespace(prefix, uri)
    }

    inner class NodeTypeTemplateBuilder :
            AbstractNodeTypeDefinitionBuilder<NodeTypeTemplate>() {
        val template: NodeTypeTemplate =
            nodeTypeManager.createNodeTypeTemplate()
        private val supertypes: MutableList<String?> = ArrayList()
        @Throws(
            UnsupportedRepositoryOperationException::class,
            RepositoryException::class)
        override fun newNodeDefinitionBuilder():
                AbstractNodeDefinitionBuilder<NodeTypeTemplate> {
            return NodeDefinitionTemplateBuilder(this)
        }

        @Throws(
            UnsupportedRepositoryOperationException::class,
            RepositoryException::class)
        override fun newPropertyDefinitionBuilder():
                AbstractPropertyDefinitionBuilder<NodeTypeTemplate> {
            return PropertyDefinitionTemplateBuilder(this)
        }

        @Throws(ConstraintViolationException::class)
        override fun build(): NodeTypeTemplate {
            template.isMixin = super.isMixin
            template.setOrderableChildNodes(super.isOrderable)
            template.isAbstract = super.isAbstract
            template.isQueryable = super.queryable
            template.setDeclaredSuperTypeNames(supertypes.toTypedArray())
            return template
        }

        override var name: String?
        get() = super.name
        @Throws(RepositoryException::class)
        set(name) {
            super.name = name
            template.name = name
        }

        override fun addSupertype(name: String?) {
            supertypes.add(name)
        }

        @Throws(ConstraintViolationException::class)
        override fun setPrimaryItemName(name: String?) {
            template.primaryItemName = name
        }

    }

    inner class PropertyDefinitionTemplateBuilder(
            private val ntd: NodeTypeTemplateBuilder) :
        AbstractPropertyDefinitionBuilder<NodeTypeTemplate>() {
        private val template: PropertyDefinitionTemplate =
            nodeTypeManager.createPropertyDefinitionTemplate()
        private val values: MutableList<Value> = ArrayList()
        private val constraints: MutableList<String?> = ArrayList()


        override var name: String?
        get() = super.name
        @Throws(RepositoryException::class)
        set(name) {
            super.name = name
            template.name = name
        }

        @Throws(ValueFormatException::class)
        override fun addDefaultValues(value: String?) {
            values.add(valueFactory.createValue(value, requiredType))
        }

        override fun addValueConstraint(constraint: String?) {
            constraints.add(constraint)
        }

        override fun setDeclaringNodeType(name: String?) {
            // empty
        }

        @Throws(IllegalStateException::class)
        override fun build() {
            template.isAutoCreated = super.autocreate
            template.isMandatory = super.isMandatory
            template.onParentVersion = super.onParent
            template.isProtected = super.isProtected
            template.requiredType = super.requiredType
            template.valueConstraints = constraints.toTypedArray()
            template.defaultValues = values.toTypedArray()
            template.isMultiple = super.isMultiple
            template.availableQueryOperators = super.queryOperators
            template.isFullTextSearchable = super.fullTextSearchable
            template.isQueryOrderable = super.queryOrderable
            ntd.template.propertyDefinitionTemplates.add(template)
        }

    }

    inner class NodeDefinitionTemplateBuilder(
            private val ntd: NodeTypeTemplateBuilder) :
        AbstractNodeDefinitionBuilder<NodeTypeTemplate>() {
        private val template: NodeDefinitionTemplate =
            nodeTypeManager.createNodeDefinitionTemplate()
        private val requiredPrimaryTypes: MutableList<String?> = ArrayList()

        override var name: String?
        get() = super.name
        @Throws(RepositoryException::class)
        set(name) {
            super.name = name
            template.name = name
        }

        override fun addRequiredPrimaryType(name: String?) {
            requiredPrimaryTypes.add(name)
        }

        @Throws(ConstraintViolationException::class)
        override fun setDefaultPrimaryType(name: String?) {
            template.defaultPrimaryTypeName = name
        }

        override fun setDeclaringNodeType(name: String?) {
            // empty
        }

        @Throws(ConstraintViolationException::class)
        override fun build() {
            template.isAutoCreated = super.autocreate
            template.isMandatory = super.isMandatory
            template.onParentVersion = super.onParent
            template.isProtected = super.isProtected
            template.requiredPrimaryTypeNames = requiredPrimaryTypes
                .toTypedArray()
            template.setSameNameSiblings(super.allowSns)
            ntd.template.nodeDefinitionTemplates.add(template)
        }
    }
}
