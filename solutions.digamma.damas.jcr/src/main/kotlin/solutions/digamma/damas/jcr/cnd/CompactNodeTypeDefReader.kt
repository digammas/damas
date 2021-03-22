package solutions.digamma.damas.jcr.cnd

import solutions.digamma.damas.jcr.cnd.DefinitionBuilderFactory.AbstractNodeDefinitionBuilder
import solutions.digamma.damas.jcr.cnd.DefinitionBuilderFactory.AbstractNodeTypeDefinitionBuilder
import solutions.digamma.damas.jcr.cnd.DefinitionBuilderFactory.AbstractPropertyDefinitionBuilder
import java.io.Reader
import java.util.LinkedList
import javax.jcr.PropertyType
import javax.jcr.RepositoryException
import javax.jcr.query.qom.QueryObjectModelConstants
import javax.jcr.version.OnParentVersionAction

class CompactNodeTypeDefReader<T, N>(
    r: Reader,
    nsMapping: N?,
    /**
     * The builder for QNodeTypeDefinitions
     */
    private val factory: DefinitionBuilderFactory<T, N>
) {
    /**
     * the list of parsed QNodeTypeDefinition
     */
    private val nodeTypeDefs: MutableList<T> = LinkedList()

    /**
     * the underlying lexer
     */
    private val lexer: Lexer = Lexer(r)

    /**
     * the current token
     */
    private var currentToken: String? = null

    /**
     * Creates a new CND reader and parses the given stream.
     *
     * @param r        a reader to the CND
     * @param factory  builder for creating new definitions and handling namespaces
     * @throws ParseException if an error occurs
     */
    constructor(
        r: Reader,
        factory: DefinitionBuilderFactory<T, N>
    ) : this(r, null, factory)

    /**
     * Returns the list of parsed node type definitions definitions.
     *
     * @return a collection of node type definition objects
     */
    val nodeTypeDefinitions: List<T>
    get() = nodeTypeDefs

    /**
     * Returns the namespace mapping.
     *
     * @return
     */
    val namespaceMapping: N
    get() = factory.namespaceMapping

    /**
     * Parses the definition
     *
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun parse() {
        while (!currentTokenEquals(Lexer.EOF)) {
            if (!doNameSpace()) {
                break
            }
        }
        try {
            while (!currentTokenEquals(Lexer.EOF)) {
                val ntd = factory.newNodeTypeDefinitionBuilder()
                ntd.setOrderableChildNodes(false)
                ntd.isMixin = false
                ntd.isAbstract = false
                ntd.queryable = true
                doNodeTypeName(ntd)
                doSuperTypes(ntd)
                doOptions(ntd)
                doItemDefs(ntd)
                nodeTypeDefs.add(ntd.build())
            }
        } catch (e: RepositoryException) {
            lexer.fail(e)
        }
    }

    /**
     * processes the namespace declaration
     *
     * @return `true` if a namespace was parsed
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doNameSpace(): Boolean {
        if (!currentTokenEquals('<')) {
            return false
        }
        nextToken()
        val prefix = currentToken
        nextToken()
        if (!currentTokenEquals('=')) {
            lexer.fail("Missing = in namespace decl.")
        }
        nextToken()
        val uri = currentToken
        nextToken()
        if (!currentTokenEquals('>')) {
            lexer.fail("Missing > in namespace decl.")
        }
        try {
            factory.setNamespace(prefix, uri)
        } catch (e: RepositoryException) {
            lexer.fail("Error setting namespace mapping $currentToken", e)
        }
        nextToken()
        return true
    }

    /**
     * processes the nodetype name
     *
     * @param ntd nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doNodeTypeName(ntd: AbstractNodeTypeDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.BEGIN_NODE_TYPE_NAME)) {
            lexer.fail(
                "Missing '${Lexer.BEGIN_NODE_TYPE_NAME}' delimiter for beginning of node type name"
            )
        }
        nextToken()
        try {
            ntd!!.name = currentToken
        } catch (e: RepositoryException) {
            lexer.fail("Error setting node type name $currentToken", e)
        }
        nextToken()
        if (!currentTokenEquals(Lexer.END_NODE_TYPE_NAME)) {
            lexer.fail(
                "Missing '${Lexer.END_NODE_TYPE_NAME}' delimiter for end of node type name, found $currentToken"
            )
        }
        nextToken()
    }

    /**
     * processes the superclasses
     *
     * @param ntd nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doSuperTypes(ntd: AbstractNodeTypeDefinitionBuilder<T>?) {
        if (currentTokenEquals(Lexer.EXTENDS)) do {
            nextToken()
            try {
                ntd!!.addSupertype(currentToken)
            } catch (e: RepositoryException) {
                lexer.fail(
                    "Error setting supertype of ${ntd!!.name} to $currentToken",
                    e)
            }
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    /**
     * processes the options
     *
     * @param ntd nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doOptions(ntd: AbstractNodeTypeDefinitionBuilder<T>?) {
        var hasOption = true
        try {
            while (hasOption) {
                if (currentTokenEquals(Lexer.ORDERABLE)) {
                    nextToken()
                    ntd!!.setOrderableChildNodes(true)
                } else if (currentTokenEquals(Lexer.MIXIN)) {
                    nextToken()
                    ntd!!.isMixin = true
                } else if (currentTokenEquals(Lexer.ABSTRACT)) {
                    nextToken()
                    ntd!!.isAbstract = true
                } else if (currentTokenEquals(Lexer.NOQUERY)) {
                    nextToken()
                    ntd!!.queryable = false
                } else if (currentTokenEquals(Lexer.QUERY)) {
                    nextToken()
                    ntd!!.queryable = true
                } else if (currentTokenEquals(Lexer.PRIMARYITEM)) {
                    nextToken()
                    ntd!!.setPrimaryItemName(currentToken)
                    nextToken()
                } else {
                    hasOption = false
                }
            }
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error setting option of ${ntd!!.name} to $currentToken", e)
        }
    }

    /**
     * processes the item definitions
     *
     * @param ntd nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doItemDefs(ntd: AbstractNodeTypeDefinitionBuilder<T>?) {
        while (
                currentTokenEquals(Lexer.PROPERTY_DEFINITION) ||
                currentTokenEquals(Lexer.CHILD_NODE_DEFINITION)) {
            if (currentTokenEquals(Lexer.PROPERTY_DEFINITION)) {
                try {
                    val pd = ntd!!.newPropertyDefinitionBuilder()
                    try {
                        pd!!.setAutoCreated(false)
                        pd.setDeclaringNodeType(ntd.name)
                        pd.isMandatory = false
                        pd.isMultiple = false
                        pd.setOnParentVersion(OnParentVersionAction.COPY)
                        pd.isProtected = false
                        pd.requiredType = PropertyType.STRING
                        pd.fullTextSearchable = true
                        pd.queryOrderable = true
                    } catch (e: RepositoryException) {
                        lexer.fail(
                            "Error setting property definitions of ${pd!!.name} to $currentToken",
                            e)
                    }
                    nextToken()
                    doPropertyDefinition(pd, ntd)
                    pd!!.build()
                } catch (e: RepositoryException) {
                    lexer.fail(
                        "Error building property definition for ${ntd!!.name}",
                        e)
                }
            } else if (currentTokenEquals(Lexer.CHILD_NODE_DEFINITION)) {
                try {
                    val nd = ntd!!.newNodeDefinitionBuilder()
                    try {
                        nd!!.setAllowsSameNameSiblings(false)
                        nd.setAutoCreated(false)
                        nd.setDeclaringNodeType(ntd.name)
                        nd.isMandatory = false
                        nd.setOnParentVersion(OnParentVersionAction.COPY)
                        nd.isProtected = false
                    } catch (e: RepositoryException) {
                        lexer.fail(
                            "Error setting node definitions of ${nd!!.name} to $currentToken",
                            e)
                    }
                    nextToken()
                    doChildNodeDefinition(nd, ntd)
                    nd!!.build()
                } catch (e: RepositoryException) {
                    lexer.fail(
                        "Error building node definition for ${ntd!!.name}", e)
                }
            }
        }
    }

    /**
     * processes the property definition
     *
     * @param pd  property definition builder
     * @param ntd declaring nodetype definition builder
     * @throws ParseException if an error during parsing occur
     */
    @Throws(ParseException::class)
    private fun doPropertyDefinition(
        pd: AbstractPropertyDefinitionBuilder<T>?,
        ntd: AbstractNodeTypeDefinitionBuilder<T>?
    ) {
        try {
            pd!!.name = currentToken
        } catch (e: RepositoryException) {
            lexer.fail("Invalid property name '$currentToken", e)
        }
        nextToken()
        doPropertyType(pd)
        doPropertyDefaultValue(pd)
        doPropertyAttributes(pd, ntd)
        doPropertyValueConstraints(pd)
    }

    /**
     * processes the property type
     *
     * @param pd property definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doPropertyType(pd: AbstractPropertyDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.BEGIN_TYPE)) {
            return
        }
        nextToken()
        try {
            pd!!.requiredType = when {
                currentTokenEquals(Lexer.STRING) -> PropertyType.STRING
                currentTokenEquals(Lexer.BINARY) -> PropertyType.BINARY
                currentTokenEquals(Lexer.LONG) -> PropertyType.LONG
                currentTokenEquals(Lexer.DECIMAL) -> PropertyType.DECIMAL
                currentTokenEquals(Lexer.DOUBLE) -> PropertyType.DOUBLE
                currentTokenEquals(Lexer.BOOLEAN) -> PropertyType.BOOLEAN
                currentTokenEquals(Lexer.DATE) -> PropertyType.DATE
                currentTokenEquals(Lexer.NAME) -> PropertyType.NAME
                currentTokenEquals(Lexer.PATH) -> PropertyType.PATH
                currentTokenEquals(Lexer.URI) -> PropertyType.URI
                currentTokenEquals(Lexer.REFERENCE) -> PropertyType.REFERENCE
                currentTokenEquals(Lexer.WEAKREFERENCE) ->
                    PropertyType.WEAKREFERENCE
                currentTokenEquals(Lexer.UNDEFINED) -> PropertyType.UNDEFINED
                else -> lexer.fail(
                        "Unknown property type '$currentToken' specified")
            }
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error setting property type of ${pd!!.name} to $currentToken",
                e)
        }
        nextToken()
        if (!currentTokenEquals(Lexer.END_TYPE)) {
            lexer.fail(
                "Missing '${Lexer.END_TYPE}' delimiter for end of property type"
            )
        }
        nextToken()
    }

    /**
     * processes the property attributes
     *
     * @param pd  property definition builder
     * @param ntd declaring nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doPropertyAttributes(
        pd: AbstractPropertyDefinitionBuilder<T>?,
        ntd: AbstractNodeTypeDefinitionBuilder<T>?
    ) {
        try {
            while (currentTokenEquals(Lexer.PROP_ATTRIBUTE)) {
                when {
                    currentTokenEquals(Lexer.PRIMARY) -> {
                        ntd!!.setPrimaryItemName(pd!!.name)
                    }
                    currentTokenEquals(Lexer.AUTOCREATED) -> {
                        pd!!.setAutoCreated(true)
                    }
                    currentTokenEquals(Lexer.MANDATORY) -> {
                        pd!!.isMandatory = true
                    }
                    currentTokenEquals(Lexer.PROTECTED) -> {
                        pd!!.isProtected = true
                    }
                    currentTokenEquals(Lexer.MULTIPLE) -> {
                        pd!!.isMultiple = true
                    }
                    currentTokenEquals(Lexer.COPY) -> {
                        pd!!.setOnParentVersion(OnParentVersionAction.COPY)
                    }
                    currentTokenEquals(Lexer.VERSION) -> {
                        pd!!.setOnParentVersion(OnParentVersionAction.VERSION)
                    }
                    currentTokenEquals(Lexer.INITIALIZE) -> {
                        pd!!.setOnParentVersion(
                            OnParentVersionAction.INITIALIZE)
                    }
                    currentTokenEquals(Lexer.COMPUTE) -> {
                        pd!!.setOnParentVersion(OnParentVersionAction.COMPUTE)
                    }
                    currentTokenEquals(Lexer.IGNORE) -> {
                        pd!!.setOnParentVersion(OnParentVersionAction.IGNORE)
                    }
                    currentTokenEquals(Lexer.ABORT) -> {
                        pd!!.setOnParentVersion(OnParentVersionAction.ABORT)
                    }
                    currentTokenEquals(Lexer.NOFULLTEXT) -> {
                        pd!!.fullTextSearchable = false
                    }
                    currentTokenEquals(Lexer.NOQUERYORDER) -> {
                        pd!!.queryOrderable = false
                    }
                    currentTokenEquals(Lexer.QUERYOPS) -> {
                        doPropertyQueryOperators(pd)
                    }
                }
                nextToken()
            }
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error setting property attribute of ${pd!!.name} to $currentToken",
                e)
        }
    }

    /**
     * processes the property query operators
     *
     * @param pd the property definition builder
     * @throws ParseException if an error occurs
     */
    @Throws(ParseException::class)
    private fun doPropertyQueryOperators(pd: AbstractPropertyDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.QUERYOPS)) {
            return
        }
        nextToken()
        val ops = currentToken!!.split(",".toRegex()).toTypedArray()
        val queryOps: MutableList<String> = LinkedList()
        for (op in ops) {
            val s = op.trim { it <= ' ' }
            queryOps.add(when (s) {
                Lexer.QUEROPS_EQUAL ->
                    QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO
                Lexer.QUEROPS_NOTEQUAL ->
                    QueryObjectModelConstants.JCR_OPERATOR_NOT_EQUAL_TO
                Lexer.QUEROPS_LESSTHAN ->
                    QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN
                Lexer.QUEROPS_LESSTHANOREQUAL ->
                    QueryObjectModelConstants.JCR_OPERATOR_LESS_THAN_OR_EQUAL_TO
                Lexer.QUEROPS_GREATERTHAN ->
                    QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN
                Lexer.QUEROPS_GREATERTHANOREQUAL ->
                    QueryObjectModelConstants.JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO
                Lexer.QUEROPS_LIKE ->
                    QueryObjectModelConstants.JCR_OPERATOR_LIKE
                else -> lexer.fail("'$s' is not a valid query operator")
            })
        }
        try {
            pd!!.setAvailableQueryOperators(queryOps.toTypedArray())
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error query operators for ${pd!!.name} to $currentToken", e)
        }
    }

    /**
     * processes the property default values
     *
     * @param pd property definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doPropertyDefaultValue(
            pd: AbstractPropertyDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.DEFAULT)) {
            return
        }
        do {
            nextToken()
            try {
                pd!!.addDefaultValues(currentToken)
            } catch (e: RepositoryException) {
                lexer.fail(
                    "Error adding default value for ${pd!!.name} to $currentToken",
                    e)
            }
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    /**
     * processes the property value constraints
     *
     * @param pd property definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doPropertyValueConstraints(
            pd: AbstractPropertyDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.CONSTRAINT)) {
            return
        }
        do {
            nextToken()
            try {
                pd!!.addValueConstraint(currentToken)
            } catch (e: RepositoryException) {
                lexer.fail(
                    "Error adding value constraint for ${pd!!.name} to $currentToken",
                    e)
            }
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    /**
     * processes the childnode definition
     *
     * @param nd  node definition builder
     * @param ntd declaring nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doChildNodeDefinition(
        nd: AbstractNodeDefinitionBuilder<T>?,
        ntd: AbstractNodeTypeDefinitionBuilder<T>?
    ) {
        try {
            nd!!.name = currentToken
        } catch (e: RepositoryException) {
            lexer.fail("Invalid child node name '$currentToken", e)
        }
        nextToken()
        doChildNodeRequiredTypes(nd)
        doChildNodeDefaultType(nd)
        doChildNodeAttributes(nd, ntd)
    }

    /**
     * processes the childnode required types
     *
     * @param nd node definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doChildNodeRequiredTypes(
            nd: AbstractNodeDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.BEGIN_TYPE)) {
            return
        }
        do {
            nextToken()
            try {
                nd!!.addRequiredPrimaryType(currentToken)
            } catch (e: RepositoryException) {
                lexer.fail(
                    "Error setting required primary type of ${nd!!.name} to $currentToken",
                    e)
            }
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
        nextToken()
    }

    /**
     * processes the childnode default types
     *
     * @param nd node definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doChildNodeDefaultType(nd: AbstractNodeDefinitionBuilder<T>?) {
        if (!currentTokenEquals(Lexer.DEFAULT)) {
            return
        }
        nextToken()
        try {
            nd!!.setDefaultPrimaryType(currentToken)
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error setting default primary type of ${nd!!.name} to $currentToken",
                e)
        }
        nextToken()
    }

    /**
     * processes the childnode attributes
     *
     * @param nd  node definition builder
     * @param ntd declaring nodetype definition builder
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun doChildNodeAttributes(
        nd: AbstractNodeDefinitionBuilder<T>?,
        ntd: AbstractNodeTypeDefinitionBuilder<T>?
    ) {
        try {
            while (currentTokenEquals(Lexer.NODE_ATTRIBUTE)) {
                when {
                    currentTokenEquals(Lexer.PRIMARY) -> {
                        ntd!!.setPrimaryItemName(nd!!.name)
                    }
                    currentTokenEquals(Lexer.AUTOCREATED) -> {
                        nd!!.setAutoCreated(true)
                    }
                    currentTokenEquals(Lexer.MANDATORY) -> {
                        nd!!.isMandatory = true
                    }
                    currentTokenEquals(Lexer.PROTECTED) -> {
                        nd!!.isProtected = true
                    }
                    currentTokenEquals(Lexer.SNS) -> {
                        nd!!.setAllowsSameNameSiblings(true)
                    }
                    currentTokenEquals(Lexer.COPY) -> {
                        nd!!.setOnParentVersion(OnParentVersionAction.COPY)
                    }
                    currentTokenEquals(Lexer.VERSION) -> {
                        nd!!.setOnParentVersion(OnParentVersionAction.VERSION)
                    }
                    currentTokenEquals(Lexer.INITIALIZE) -> {
                        nd!!.setOnParentVersion(
                            OnParentVersionAction.INITIALIZE)
                    }
                    currentTokenEquals(Lexer.COMPUTE) -> {
                        nd!!.setOnParentVersion(OnParentVersionAction.COMPUTE)
                    }
                    currentTokenEquals(Lexer.IGNORE) -> {
                        nd!!.setOnParentVersion(OnParentVersionAction.IGNORE)
                    }
                    currentTokenEquals(Lexer.ABORT) -> {
                        nd!!.setOnParentVersion(OnParentVersionAction.ABORT)
                    }
                }
                nextToken()
            }
        } catch (e: RepositoryException) {
            lexer.fail(
                "Error setting child node attribute of ${nd!!.name} to $currentToken",
                e)
        }
    }

    /**
     * Gets the next token from the underlying lexer.
     *
     * @throws ParseException if the lexer fails to get the next token.
     * @see Lexer.getNextToken
     */
    @Throws(ParseException::class)
    private fun nextToken() {
        currentToken = lexer.nextToken
    }

    /**
     * Checks if the [.currentToken] is semantically equal to the given
     * argument ignoring the case.
     *
     * @param s the tokens to compare with
     * @return `true` if equals; `false` otherwise.
     */
    private fun currentTokenEquals(s: Array<String>): Boolean {
        return s.any {
            this.currentToken.equals(it, ignoreCase = true)
        }
    }

    /**
     * Checks if the [.currentToken] is semantically equal to the given
     * argument.
     *
     * @param c the tokens to compare with
     * @return `true` if equals; `false` otherwise.
     */
    private fun currentTokenEquals(c: Char): Boolean {
        return currentToken!!.length == 1 && currentToken!![0] == c
    }

    /**
     * Checks if the [.currentToken] is semantically equal to the given
     * argument.
     *
     * @param s the tokens to compare with
     * @return `true` if equals; `false` otherwise.
     */
    private fun currentTokenEquals(s: String): Boolean {
        return currentToken == s
    }

    /**
     * Creates a new CND reader and parses the given stream.
     *
     * @param r         a reader to the CND
     * @param systemId  a informative id of the given stream
     * @param nsMapping default namespace mapping to use
     * @param factory   builder for creating new definitions and handling
     *      namespaces
     * @throws ParseException if an error occurs
     */
    init {
        if (nsMapping != null) {
            factory.namespaceMapping = nsMapping
        }
        nextToken()
        parse()
    }
}
