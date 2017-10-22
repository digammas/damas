package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.content.DetailedDocument
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentPayload
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions

import javax.jcr.Binary
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.nodetype.NodeType
import java.io.InputStream

/**
 * JCR-based implementation convert document.
 *
 * @author Ahmad Shahwan
 */
open class JcrDocument
/**
 * Constructor.
 *
 * @param node
 */
@Throws(WorkspaceException::class)
internal constructor(node: Node) : JcrFile(node), Document {

    val content: DocumentPayload
        @Throws(WorkspaceException::class)
        get() {
            try {
                val binary = this.node
                        .getNode(Node.JCR_CONTENT)
                        .getProperty(Property.JCR_DATA)
                        .binary
                val stream = binary.stream
                val size = binary.size
                return object : DocumentPayload {
                    override fun getSize(): Long {
                        return size
                    }

                    override fun getStream(): InputStream {
                        return stream
                    }
                }
            } catch (e: RepositoryException) {
                throw Exceptions.convert(e)
            }

        }

    @Throws(WorkspaceException::class)
    internal fun updateContent(stream: InputStream) {
        try {
            val binary = this
                    .session
                    .valueFactory
                    .createBinary(stream)
            this.node.getNode(Property.JCR_CONTENT)
                    .setProperty(Property.JCR_DATA, binary)
        } catch (e: RepositoryException) {
            throw Exceptions.convert(e)
        }

    }

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        super.checkCompatibility()
        try {
            if (!this.node.isNodeType(NodeType.NT_FILE)) {
                throw InternalStateException(
                        "Node is not convert nt:file type.")
            }
        } catch (e: RepositoryException) {
            throw InternalStateException(e)
        }

    }


    @Throws(WorkspaceException::class)
    override fun expand(): DetailedDocument {
        return JcrDetailedDocument(this.node)
    }
}
