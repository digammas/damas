package solutions.digamma.damas.jcr.cnd

import java.io.StreamTokenizer
import java.io.IOException
import java.io.Reader
import java.lang.Exception
import java.text.DecimalFormat

/**
 * Lexer of the CND definition.
 */
class Lexer(
    r: Reader?
) {
    companion object {
        const val SINGLE_QUOTE = '\''
        const val DOUBLE_QUOTE = '\"'
        const val BEGIN_NODE_TYPE_NAME = '['
        const val END_NODE_TYPE_NAME = ']'
        const val EXTENDS = '>'
        const val LIST_DELIMITER = ','
        const val PROPERTY_DEFINITION = '-'
        const val CHILD_NODE_DEFINITION = '+'
        const val BEGIN_TYPE = '('
        const val END_TYPE = ')'
        const val DEFAULT = '='
        const val CONSTRAINT = '<'
        val ORDERABLE = arrayOf("orderable", "ord", "o")
        val MIXIN = arrayOf("mixin", "mix", "m")
        val ABSTRACT = arrayOf("abstract", "abs", "a")
        val NOQUERY = arrayOf("noquery", "nq")
        val QUERY = arrayOf("query", "q")
        val PRIMARYITEM = arrayOf("primaryitem", "!")
        val PRIMARY = arrayOf("primary", "pri", "!")
        val AUTOCREATED = arrayOf("autocreated", "aut", "a")
        val MANDATORY = arrayOf("mandatory", "man", "m")
        val PROTECTED = arrayOf("protected", "pro", "p")
        val MULTIPLE = arrayOf("multiple", "mul", "*")
        val SNS = arrayOf("sns", "*", "multiple")
        val QUERYOPS = arrayOf("queryops", "qop")
        val NOFULLTEXT = arrayOf("nofulltext", "nof")
        val NOQUERYORDER = arrayOf("noqueryorder", "nqord")
        val COPY = arrayOf("COPY")
        val VERSION = arrayOf("VERSION")
        val INITIALIZE = arrayOf("INITIALIZE")
        val COMPUTE = arrayOf("COMPUTE")
        val IGNORE = arrayOf("IGNORE")
        val ABORT = arrayOf("ABORT")
        val PROP_ATTRIBUTE = arrayOf(
            *PRIMARY, *AUTOCREATED, *MANDATORY, *PROTECTED, *MULTIPLE,
            *QUERYOPS, *NOFULLTEXT, *NOQUERYORDER, *COPY, *VERSION, *INITIALIZE,
            *COMPUTE, *IGNORE, *ABORT,
        )
        val NODE_ATTRIBUTE = arrayOf(
            *PRIMARY, *AUTOCREATED, *MANDATORY, *PROTECTED, *SNS, *COPY,
            *VERSION, *INITIALIZE, *COMPUTE, *IGNORE, *ABORT,
        )
        const val QUEROPS_EQUAL = "="
        const val QUEROPS_NOTEQUAL = "<>"
        const val QUEROPS_LESSTHAN = "<"
        const val QUEROPS_LESSTHANOREQUAL = "<="
        const val QUEROPS_GREATERTHAN = ">"
        const val QUEROPS_GREATERTHANOREQUAL = ">="
        const val QUEROPS_LIKE = "LIKE"
        val STRING = arrayOf("STRING")
        val BINARY = arrayOf("BINARY")
        val LONG = arrayOf("LONG")
        val DOUBLE = arrayOf("DOUBLE")
        val BOOLEAN = arrayOf("BOOLEAN")
        val DATE = arrayOf("DATE")
        val NAME = arrayOf("NAME")
        val PATH = arrayOf("PATH")
        val REFERENCE = arrayOf("REFERENCE")
        val WEAKREFERENCE = arrayOf("WEAKREFERENCE")
        val URI = arrayOf("URI")
        val DECIMAL = arrayOf("DECIMAL")
        val UNDEFINED = arrayOf("UNDEFINED", "*")
        const val EOF = "eof"
    }

    private val st: StreamTokenizer = StreamTokenizer(r)

    /**
     * getNextToken
     *
     * @return the next token
     * @throws ParseException if an error during parsing occurs
     */
    val nextToken: String?
    @Throws(ParseException::class)
    get() = try {
        val tokenType = st.nextToken()
        if (tokenType == StreamTokenizer.TT_EOF) {
            EOF
        } else if (tokenType == StreamTokenizer.TT_WORD ||
            tokenType == SINGLE_QUOTE.toInt() ||
            tokenType == DOUBLE_QUOTE.toInt()) {
            st.sval
        } else if (tokenType == StreamTokenizer.TT_NUMBER) {
            DecimalFormat("#.###").format(st.nval)
        } else {
            String(charArrayOf(tokenType.toChar()))
        }
    } catch (e: IOException) {
        fail("IOException while attempting to read input stream", e)
        null
    }

    private val lineNumber: Int
    get() = st.lineno()

    /**
     * Creates a failure exception including the current line number and systemid.
     * @param message message
     * @throws ParseException the created exception
     */
    @Throws(ParseException::class)
    fun fail(message: String): Nothing {
        throw ParseException(message, lineNumber, -1)
    }

    /**
     * Creates a failure exception including the current line number and systemid.
     * @param message message
     * @param e root cause
     * @throws ParseException the created exception
     */
    @Throws(ParseException::class)
    fun fail(message: String, e: Exception) {
        throw ParseException(message, e, lineNumber, -1)
    }

    /**
     * Creates a failure exception including the current line number and systemid.
     * @param e root cause
     * @throws ParseException the created exception
     */
    @Throws(ParseException::class)
    fun fail(e: Exception) {
        throw ParseException(e, lineNumber, -1)
    }

    /**
     * Creates an unitialized lexer on top of the given reader.
     * @param r the reader
     * @param systemId informational systemid of the given stream
     */
    init {
        st.eolIsSignificant(false)
        st.lowerCaseMode(false)
        st.slashSlashComments(true)
        st.slashStarComments(true)
        st.wordChars('a'.toInt(), 'z'.toInt())
        st.wordChars('A'.toInt(), 'Z'.toInt())
        st.wordChars(':'.toInt(), ':'.toInt())
        st.wordChars('_'.toInt(), '_'.toInt())
        st.quoteChar(SINGLE_QUOTE.toInt())
        st.quoteChar(DOUBLE_QUOTE.toInt())
        st.ordinaryChar(BEGIN_NODE_TYPE_NAME.toInt())
        st.ordinaryChar(END_NODE_TYPE_NAME.toInt())
        st.ordinaryChar(EXTENDS.toInt())
        st.ordinaryChar(LIST_DELIMITER.toInt())
        st.ordinaryChar(PROPERTY_DEFINITION.toInt())
        st.ordinaryChar(CHILD_NODE_DEFINITION.toInt())
        st.ordinaryChar(BEGIN_TYPE.toInt())
        st.ordinaryChar(END_TYPE.toInt())
        st.ordinaryChar(DEFAULT.toInt())
        st.ordinaryChar(CONSTRAINT.toInt())
    }
}
