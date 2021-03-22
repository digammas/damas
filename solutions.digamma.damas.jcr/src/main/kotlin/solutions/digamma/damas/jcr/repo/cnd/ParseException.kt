package solutions.digamma.damas.jcr.repo.cnd

import solutions.digamma.damas.common.WorkspaceException
import java.lang.StringBuffer

/**
 * ParseException
 */
class ParseException : WorkspaceException {
    /**
     * the line number where the error occurred
     */
    private val lineNumber: Int

    /**
     * the column number where the error occurred
     */
    private val colNumber: Int

    /**
     * Constructs a new instance of this class with `null` as its
     * detail message.
     * @param lineNumber line number
     * @param colNumber columns number
     * @param systemId system id
     */
    constructor(lineNumber: Int, colNumber: Int) : super() {
        this.lineNumber = lineNumber
        this.colNumber = colNumber
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detail message. The detail message is saved for
     * later retrieval by the [.getMessage] method.
     * @param lineNumber line number
     * @param colNumber columns number
     * @param systemId system id
     */
    constructor(
            message: String?,
            lineNumber: Int,
            colNumber: Int,
    ) : super(message) {
        this.lineNumber = lineNumber
        this.colNumber = colNumber
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message   the detail message. The detail message is saved for
     * later retrieval by the [.getMessage] method.
     * @param lineNumber line number
     * @param colNumber columns number
     * @param systemId system id
     * @param rootCause root failure cause
     */
    constructor(
        message: String,
        rootCause: Exception,
        lineNumber: Int,
        colNumber: Int,
    ) : super(
        message,
        rootCause
    ) {
        this.lineNumber = lineNumber
        this.colNumber = colNumber
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param lineNumber line number
     * @param colNumber columns number
     * @param systemId system id
     * @param rootCause root failure cause
     */
    constructor(
        rootCause: Exception,
        lineNumber: Int,
        colNumber: Int,
    ) : super(rootCause) {
        this.lineNumber = lineNumber
        this.colNumber = colNumber
    }

    override val message: String
    get() {
        val message = super.message
        val b = StringBuffer(message ?: "")
        var delim = " ("
        if (lineNumber >= 0) {
            b.append(delim)
            b.append("line ")
            b.append(lineNumber)
            delim = ", "
        }
        if (colNumber >= 0) {
            b.append(delim)
            b.append("col ")
            b.append(colNumber)
            delim = ", "
        }
        if (delim == ", ") {
            b.append(")")
        }
        return b.toString()
    }
}
