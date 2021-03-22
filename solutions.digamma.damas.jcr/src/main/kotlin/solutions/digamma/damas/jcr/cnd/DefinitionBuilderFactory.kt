package solutions.digamma.damas.jcr.cnd

import javax.jcr.RepositoryException
import java.lang.NullPointerException
import javax.jcr.PropertyType
import javax.jcr.query.qom.QueryObjectModelConstants

/**
 * Builder for node type definitions, node definitions and property definitions.
 * @param <T>  type of the node type definition
 * @param <N>  type of the namespace mapping
</N></T> */
abstract class DefinitionBuilderFactory<T, N> {
    /**
     * Create a new instance of a [AbstractNodeTypeDefinitionBuilder]
     * @return
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    abstract fun newNodeTypeDefinitionBuilder(): AbstractNodeTypeDefinitionBuilder<T>
    /**
     * @return  the namespace mapping used for the node type definition being built
     */
    /**
     * Set the namespace mapping to use for the node type definition being built
     * @param nsMapping
     */
    abstract var namespaceMapping: N

    /**
     * Add a mapping to the namespace map
     * @param prefix
     * @param uri
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    abstract fun setNamespace(prefix: String?, uri: String?)

    /**
     * Builder for a node type definition of type T.
     * @param <T>
    </T> */
    abstract class AbstractNodeTypeDefinitionBuilder<T> {
        /**
         * Returns the name of the node type definition being built
         * @return
         */
        /**
         * Set the name of the node type definition being built
         * @param name
         * @throws RepositoryException  if the name is not valid
         * @see NodeTypeDefinition.getName
         */
        @set:Throws(RepositoryException::class)
        open var name: String? = null

        @set:Throws(RepositoryException::class)
        var isMixin = false

        @set:Throws(RepositoryException::class)
        var isOrderable = false

        @set:Throws(RepositoryException::class)
        var isAbstract = false

        @set:Throws(RepositoryException::class)
        var queryable = false

        /**
         * Add the given name to the set of supertypes of the node type definition
         * being built
         * @param name  name of the the supertype
         * @throws RepositoryException  if the name is not valid
         * @see NodeTypeDefinition.getDeclaredSupertypeNames
         */
        @Throws(RepositoryException::class)
        abstract fun addSupertype(name: String?)

        /**
         * @param isOrderable `true` if building a node type having
         * orderable child nodes; `false` otherwise.
         * @throws RepositoryException
         * @see NodeTypeDefinition.hasOrderableChildNodes
         */
        @Throws(RepositoryException::class)
        fun setOrderableChildNodes(isOrderable: Boolean) {
            this.isOrderable = isOrderable
        }

        /**
         * @param name  the name of the primary item.
         * @throws RepositoryException
         * @see NodeTypeDefinition.getPrimaryItemName
         */
        @Throws(RepositoryException::class)
        abstract fun setPrimaryItemName(name: String?)

        /**
         * Create a new instance of a [AbstractPropertyDefinitionBuilder]
         * which can be used to add property definitions to the node type definition being built.
         * @return
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun newPropertyDefinitionBuilder(): AbstractPropertyDefinitionBuilder<T>?

        /**
         * Create a new instance fo a [AbstractNodeDefinitionBuilder]
         * which can be used to add child node definitions to the node type definition being built.
         * @return
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun newNodeDefinitionBuilder(): AbstractNodeDefinitionBuilder<T>?

        /**
         * Build this node type definition
         * @return
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun build(): T
    }

    /**
     * Builder for item definitions of type `T`
     * @param <T>
    </T> */
    abstract class AbstractItemDefinitionBuilder<T> {
        /**
         * Name of the child item definition being built
         *
         * @param name  the name of the child item definition being build
         * @throws RepositoryException
         * @see ItemDefinition.getName
         */
        @set:Throws(RepositoryException::class)
        open var name: String? = null

        protected var autocreate = false

        protected var onParent = 0

        /**
         * @param isProtected `true` if building a 'protected' child
         * item definition, false otherwise.
         * @throws RepositoryException
         * @see ItemDefinition.isProtected
         */
        @set:Throws(RepositoryException::class)
        var isProtected = false

        /**
         * @param isMandatory `true` if building a 'mandatory' child
         * item definition, false otherwise.
         * @throws RepositoryException
         */
        @set:Throws(RepositoryException::class)
        var isMandatory = false

        /**
         * @param name the name of the declaring node type.
         * @throws RepositoryException
         * @see ItemDefinition.getDeclaringNodeType
         */
        @Throws(RepositoryException::class)
        abstract fun setDeclaringNodeType(name: String?)

        /**
         * @param autocreate `true` if building a 'autocreate' child item
         * definition, false otherwise.
         * @throws RepositoryException
         * @see ItemDefinition.isAutoCreated
         */
        @Throws(RepositoryException::class)
        fun setAutoCreated(autocreate: Boolean) {
            this.autocreate = autocreate
        }

        /**
         * @param onParent the 'onParentVersion' attribute of the child item definition being built
         * @throws RepositoryException
         * @see ItemDefinition.getOnParentVersion
         */
        @Throws(RepositoryException::class)
        fun setOnParentVersion(onParent: Int) {
            this.onParent = onParent
        }

        /**
         * Build this item definition an add it to its parent node type definition
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun build()
    }

    /**
     * Builder for property definitions of type `T`
     * @param <T>
    </T> */
    abstract class AbstractPropertyDefinitionBuilder<T> : AbstractItemDefinitionBuilder<T>() {
        /**
         * The required type of the property definition being built.
         * @return
         */
        /**
         * @param type the required type of the property definition being built.
         * @throws RepositoryException
         * @see PropertyDefinition.getRequiredType
         */
        /** See [.setRequiredType]  */
        @set:Throws(RepositoryException::class)
        var requiredType = PropertyType.UNDEFINED

        @set:Throws(RepositoryException::class)
        var isMultiple = false

        @set:Throws(RepositoryException::class)
        var fullTextSearchable = true

        @set:Throws(RepositoryException::class)
        var queryOrderable = true

        /** See [.setAvailableQueryOperators]  */
        protected var queryOperators = ALL_OPERATORS

        /**
         * @param constraint  add a value constraint to the list of value constraints of the property
         * definition being built.
         * @throws RepositoryException
         * @see PropertyDefinition.getValueConstraints
         */
        @Throws(RepositoryException::class)
        abstract fun addValueConstraint(constraint: String?)

        /**
         * @param value  add a default value to the list of default values of the property definition
         * being built.
         * @throws RepositoryException
         * @see PropertyDefinition.getDefaultValues
         */
        @Throws(RepositoryException::class)
        abstract fun addDefaultValues(value: String?)

        /**
         * @param queryOperators the query operators of the property
         * @throws RepositoryException
         * @see PropertyDefinition.getAvailableQueryOperators
         */
        @Throws(RepositoryException::class)
        fun setAvailableQueryOperators(queryOperators: Array<String>?) {
            if (queryOperators == null) {
                throw NullPointerException("queryOperators")
            }
            this.queryOperators = queryOperators
        }

        companion object {
            private val ALL_OPERATORS = arrayOf(
                QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
                QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN,
                QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO,
                QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN,
                QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN_OR_EQUAL_TO,
                QueryObjectModelConstants.JCR_OPERATOR_LIKE,
                QueryObjectModelConstants.JCR_OPERATOR_NOT_EQUAL_TO
            )
        }
    }

    /**
     * Builder for child node definitions of type `T`
     * @param <T>
    </T> */
    abstract class AbstractNodeDefinitionBuilder<T> : AbstractItemDefinitionBuilder<T>() {
        protected var allowSns = false

        /**
         * @param name the name of the default primary type of the node definition being built.
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun setDefaultPrimaryType(name: String?)

        /**
         * @param name  add a required primary type to the list of names of the required primary types of
         * the node definition being built.
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        abstract fun addRequiredPrimaryType(name: String?)

        /**
         * @param allowSns true if building a node definition with same name siblings, false otherwise.
         * @throws RepositoryException
         */
        @Throws(RepositoryException::class)
        fun setAllowsSameNameSiblings(allowSns: Boolean) {
            this.allowSns = allowSns
        }
    }
}
