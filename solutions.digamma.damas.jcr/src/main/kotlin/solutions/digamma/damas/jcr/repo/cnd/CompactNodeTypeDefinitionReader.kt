package solutions.digamma.damas.jcr.repo.cnd

import solutions.digamma.damas.config.Configuration
import solutions.digamma.damas.config.Fallback
import solutions.digamma.damas.jcr.repo.job.ChildNodeDeclaration
import solutions.digamma.damas.jcr.repo.job.ChildPropertyDeclaration
import solutions.digamma.damas.jcr.repo.job.NamespaceDeclaration
import solutions.digamma.damas.jcr.repo.job.NodeTypeDeclaration
import solutions.digamma.damas.jcr.repo.job.RepositoryJob
import solutions.digamma.damas.logging.Logbook
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.enterprise.inject.Produces
import javax.inject.Inject
import javax.inject.Singleton
import javax.jcr.PropertyType
import javax.jcr.RepositoryException
import javax.jcr.query.qom.QueryObjectModelConstants
import javax.jcr.version.OnParentVersionAction

@Singleton
class CompactNodeTypeDefinitionReader {

    companion object {
        const val CND_PATH_CONF_KEY = "repository.cnd"
        const val DEFAULT_CND_PATH = "/repository/cnd/damas.cnd"
    }

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_ALL_CONF_KEY, optional = true)
    var skipAllInit: Boolean? = null

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_NS_CONF_KEY, optional = true)
    var skipNamespaceInit: Boolean? = null

    @Inject
    @Configuration(RepositoryJob.SKIP_INIT_NT_CONF_KEY, optional = true)
    var skipNodeTypeInit: Boolean? = null

    @Inject
    lateinit var logger: Logbook

    @Inject
    @Configuration(CND_PATH_CONF_KEY)
    @Fallback(DEFAULT_CND_PATH)
    var cndPath: String = DEFAULT_CND_PATH

    private var job: RepositoryJob? = null

    /**
     * the underlying lexer
     */
    private lateinit var lexer: Lexer

    /**
     * the current token
     */
    private var currentToken: String = "\u0000"

    val types: MutableList<NodeTypeDeclaration> = mutableListOf()

    val namespaces: MutableList<NamespaceDeclaration> = mutableListOf()

    @PostConstruct
    fun init() {
        this.logger.info("Importing type definitions and namespaces.")
        this.lexer = Lexer(
            InputStreamReader(this.javaClass.getResourceAsStream(this.cndPath))
        )
    }

    /**
     * Produce a repository jon with parsed node type and namespace
     * declarations.
     */
    @Produces
    fun produceJob(): RepositoryJob {
        return (this.job ?: this.parseIfNeeded()).also { this.job = it }
    }

    @Throws(ParseException::class)
    private fun parseIfNeeded(): RepositoryJob {
        return if (this.shouldSkip()) this.emptyJob() else parse()
    }

    private fun shouldSkip(): Boolean {
        return this.skipAllInit == true ||
            (this.skipNamespaceInit == true && this.skipNodeTypeInit == true)
    }

    private fun emptyJob() = object : RepositoryJob {}

    /**
     * Parse definitions.
     *
     * @throws ParseException if an error during parsing occurs
     */
    @Throws(ParseException::class)
    private fun parse(): RepositoryJob = this.lexer.use {
        nextToken()
        while (!currentTokenEquals(Lexer.EOF)) {
            if (!doNameSpace()) {
                break
            }
        }
        while (!currentTokenEquals(Lexer.EOF)) {
            val ntd = NodeTypeDeclaration()
            doNodeTypeName(ntd)
            doSuperTypes(ntd)
            doOptions(ntd)
            doItemDefs(ntd)
            this.types.add(ntd)
        }
        object : RepositoryJob {
            override val namespaces =
                this@CompactNodeTypeDefinitionReader.namespaces
            override val types =
                this@CompactNodeTypeDefinitionReader.types
        }
    }

    @Throws(ParseException::class)
    private fun doNameSpace(): Boolean {
        if (!currentTokenEquals(Lexer.BEGIN_NAMESPACE)) {
            return false
        }
        nextToken()
        val prefix = currentToken
        nextToken()
        if (!currentTokenEquals(Lexer.NAMESPACE_URI)) {
            lexer.fail(
                "Missing ${Lexer.NAMESPACE_URI} in namespace declaration.")
        }
        nextToken()
        val uri = currentToken
        nextToken()
        if (!currentTokenEquals(Lexer.END_NAMESPACE)) {
            lexer.fail(
                "Missing ${Lexer.END_NAMESPACE} in namespace declaration.")
        }
        this.namespaces.add(NamespaceDeclaration(prefix, uri))
        nextToken()
        return true
    }

    @Throws(ParseException::class)
    private fun doNodeTypeName(ntd: NodeTypeDeclaration) {
        if (!currentTokenEquals(Lexer.BEGIN_NODE_TYPE_NAME)) {
            lexer.fail(
                "Missing '${Lexer.BEGIN_NODE_TYPE_NAME}' delimiter for beginning of node type name"
            )
        }
        nextToken()
        ntd.name = currentToken
        nextToken()
        if (!currentTokenEquals(Lexer.END_NODE_TYPE_NAME)) {
            lexer.fail(
                "Missing '${Lexer.END_NODE_TYPE_NAME}' delimiter for end of node type name, found $currentToken"
            )
        }
        nextToken()
    }

    @Throws(ParseException::class)
    private fun doSuperTypes(ntd: NodeTypeDeclaration) {
        if (currentTokenEquals(Lexer.EXTENDS)) do {
            nextToken()
            ntd.types.add(currentToken)
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    @Throws(ParseException::class)
    private fun doOptions(ntd: NodeTypeDeclaration) {
        var hasOption = true
        while (hasOption) {
            if (currentTokenEquals(Lexer.ORDERABLE)) {
                nextToken()
                ntd.sortable = true
            } else if (currentTokenEquals(Lexer.MIXIN)) {
                nextToken()
                ntd.mixin = true
            } else if (currentTokenEquals(Lexer.ABSTRACT)) {
                nextToken()
                ntd.abstract = true
            } else if (currentTokenEquals(Lexer.NOQUERY)) {
                nextToken()
                ntd.queryable = false
            } else if (currentTokenEquals(Lexer.QUERY)) {
                nextToken()
                ntd.queryable = true
            } else if (currentTokenEquals(Lexer.PRIMARYITEM)) {
                nextToken()
                ntd.primaryItem = currentToken
                nextToken()
            } else {
                hasOption = false
            }
        }
    }

    @Throws(ParseException::class)
    private fun doItemDefs(ntd: NodeTypeDeclaration) {
        while (
                currentTokenEquals(Lexer.PROPERTY_DEFINITION) ||
                currentTokenEquals(Lexer.CHILD_NODE_DEFINITION)) {
            if (currentTokenEquals(Lexer.PROPERTY_DEFINITION)) {
                val pd = ChildPropertyDeclaration()
                nextToken()
                doPropertyDefinition(pd)
                ntd.properties.add(pd)
            } else if (currentTokenEquals(Lexer.CHILD_NODE_DEFINITION)) {
                try {
                    val nd = ChildNodeDeclaration()
                    nextToken()
                    doChildNodeDefinition(nd)
                    ntd.nodes.add(nd)
                } catch (e: RepositoryException) {
                    lexer.fail(
                        "Error building node definition for ${ntd.name}", e)
                }
            }
        }
    }

    @Throws(ParseException::class)
    private fun doPropertyDefinition(
        pd: ChildPropertyDeclaration
    ) {
        pd.name = currentToken
        nextToken()
        doPropertyType(pd)
        doPropertyDefaultValue(pd)
        doPropertyAttributes(pd)
        doPropertyValueConstraints(pd)
    }

    @Throws(ParseException::class)
    private fun doPropertyType(pd: ChildPropertyDeclaration) {
        if (!currentTokenEquals(Lexer.BEGIN_TYPE)) {
            return
        }
        nextToken()
        pd.type = when {
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
            else ->
                lexer.fail("Unknown property type '$currentToken' specified")
        }
        nextToken()
        if (!currentTokenEquals(Lexer.END_TYPE)) {
            lexer.fail(
                "Missing '${Lexer.END_TYPE}' delimiter for end of property type"
            )
        }
        nextToken()
    }

    @Throws(ParseException::class)
    private fun doPropertyAttributes(
        pd: ChildPropertyDeclaration
    ) {
        while (currentTokenEquals(Lexer.PROP_ATTRIBUTE)) {
            when {
                currentTokenEquals(Lexer.PRIMARY) -> pd.primary = true
                currentTokenEquals(Lexer.AUTOCREATED) -> pd.autocreated = true
                currentTokenEquals(Lexer.MANDATORY) -> pd.mandatory = true
                currentTokenEquals(Lexer.PROTECTED) -> pd.protected = true
                currentTokenEquals(Lexer.MULTIPLE) -> pd.multiple = true
                currentTokenEquals(Lexer.COPY) ->
                    pd.version = OnParentVersionAction.COPY
                currentTokenEquals(Lexer.VERSION) ->
                    pd.version = OnParentVersionAction.VERSION
                currentTokenEquals(Lexer.INITIALIZE) ->
                    pd.version = OnParentVersionAction.INITIALIZE
                currentTokenEquals(Lexer.COMPUTE) ->
                    pd.version = OnParentVersionAction.COMPUTE
                currentTokenEquals(Lexer.IGNORE) ->
                    pd.version = OnParentVersionAction.IGNORE
                currentTokenEquals(Lexer.ABORT) ->
                    pd.version = OnParentVersionAction.ABORT
                currentTokenEquals(Lexer.NOFULLTEXT) -> pd.searchable = false
                currentTokenEquals(Lexer.NOQUERYORDER) -> pd.queryOrder = false
                currentTokenEquals(Lexer.QUERYOPS) ->
                    doPropertyQueryOperators(pd)
            }
            nextToken()
        }
    }

    @Throws(ParseException::class)
    private fun doPropertyQueryOperators(pd: ChildPropertyDeclaration) {
        if (!currentTokenEquals(Lexer.QUERYOPS)) {
            return
        }
        nextToken()
        val ops = currentToken.split(",")
        pd.queryOperators = ops.map {
            when(it.trim()) {
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
                    QueryObjectModelConstants
                        .JCR_OPERATOR_GREATER_THAN_OR_EQUAL_TO
                Lexer.QUEROPS_LIKE ->
                    QueryObjectModelConstants.JCR_OPERATOR_LIKE
                else -> lexer.fail("'$it' is not a valid query operator")
            }
        }.toMutableList()
    }

    @Throws(ParseException::class)
    private fun doPropertyDefaultValue(
            pd: ChildPropertyDeclaration
    ) {
        if (!currentTokenEquals(Lexer.DEFAULT)) {
            return
        }
        do {
            nextToken()
            pd.defaultValues.add(currentToken)
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    @Throws(ParseException::class)
    private fun doPropertyValueConstraints(
            pd: ChildPropertyDeclaration
    ) {
        if (!currentTokenEquals(Lexer.CONSTRAINT)) {
            return
        }
        do {
            nextToken()
            pd.constraints.add(currentToken)
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
    }

    @Throws(ParseException::class)
    private fun doChildNodeDefinition(nd: ChildNodeDeclaration) {
        nd.name = currentToken
        nextToken()
        doChildNodeRequiredTypes(nd)
        doChildNodeDefaultType(nd)
        doChildNodeAttributes(nd)
    }

    @Throws(ParseException::class)
    private fun doChildNodeRequiredTypes(
            nd: ChildNodeDeclaration
    ) {
        if (!currentTokenEquals(Lexer.BEGIN_TYPE)) {
            return
        }
        do {
            nextToken()
            nd.types.add(currentToken)
            nextToken()
        } while (currentTokenEquals(Lexer.LIST_DELIMITER))
        nextToken()
    }

    @Throws(ParseException::class)
    private fun doChildNodeDefaultType(nd: ChildNodeDeclaration) {
        if (!currentTokenEquals(Lexer.DEFAULT)) {
            return
        }
        nextToken()
        nd.defaultType = currentToken
        nextToken()
    }

    @Throws(ParseException::class)
    private fun doChildNodeAttributes(nd: ChildNodeDeclaration) {
        while (currentTokenEquals(Lexer.NODE_ATTRIBUTE)) {
            when {
                currentTokenEquals(Lexer.PRIMARY) -> nd.primary = true
                currentTokenEquals(Lexer.AUTOCREATED) -> nd.autocreated = true
                currentTokenEquals(Lexer.MANDATORY) -> nd.mandatory = true
                currentTokenEquals(Lexer.PROTECTED) -> nd.protected = true
                currentTokenEquals(Lexer.SNS) -> nd.multiple = true
                currentTokenEquals(Lexer.COPY) ->
                    nd.version = OnParentVersionAction.COPY
                currentTokenEquals(Lexer.VERSION) ->
                    nd.version = OnParentVersionAction.VERSION
                currentTokenEquals(Lexer.INITIALIZE) ->
                    nd.version = OnParentVersionAction.INITIALIZE
                currentTokenEquals(Lexer.COMPUTE) ->
                    nd.version = OnParentVersionAction.COMPUTE
                currentTokenEquals(Lexer.IGNORE) ->
                    nd.version = OnParentVersionAction.IGNORE
                currentTokenEquals(Lexer.ABORT) ->
                    nd.version = OnParentVersionAction.ABORT
            }
            nextToken()
        }
    }

    /**
     * Get the next token from the underlying lexer.
     *
     * @throws ParseException if the lexer fails to get the next token.
     * @see Lexer.getNextToken
     */
    @Throws(ParseException::class)
    private fun nextToken() {
        currentToken = lexer.nextToken
    }

    /**
     * Check if the [currentToken] is semantically equal to the given
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
     * Check if the [currentToken] is semantically equal to the given
     * argument.
     *
     * @param c the tokens to compare with
     * @return `true` if equals; `false` otherwise.
     */
    private fun currentTokenEquals(c: Char): Boolean {
        return currentToken.length == 1 && currentToken[0] == c
    }

    /**
     * Checks if the [currentToken] is semantically equal to the given
     * argument.
     *
     * @param s the tokens to compare with
     * @return `true` if equals; `false` otherwise.
     */
    private fun currentTokenEquals(s: String): Boolean {
        return currentToken == s
    }
}
