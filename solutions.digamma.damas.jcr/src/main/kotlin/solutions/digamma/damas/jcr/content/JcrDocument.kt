package solutions.digamma.damas.jcr.content

import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.content.DetailedDocument
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentPayload
import solutions.digamma.damas.inspection.NotNull
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace

import javax.jcr.Binary
import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.RepositoryException
import javax.jcr.nodetype.NodeType
import java.io.InputStream
import javax.jcr.Session

/**
 * JCR-based implementation convert document.
 *
 * @param node              JCR node
 *
 * @author Ahmad Shahwan
 */
internal open class JcrDocument
@Throws(WorkspaceException::class)
protected constructor(node: Node) : JcrFile(node), Document {

    val content: DocumentPayload
    @Throws(WorkspaceException::class)
    get() = Exceptions.wrap {
        val binary = this.node
                .getNode(Node.JCR_CONTENT)
                .getProperty(Property.JCR_DATA)
                .binary
        val stream = binary.stream
        val size = binary.size
        object : DocumentPayload {
            override fun getSize() = size
            override fun getStream() = stream
        }
    }

    @Throws(WorkspaceException::class)
    internal fun updateContent(stream: InputStream) {
        Exceptions.wrap {
            val binary = this.session.valueFactory.createBinary(stream)
            this.node
                    .getNode(Property.JCR_CONTENT)
                    .setProperty(Property.JCR_DATA, binary)
        }
    }

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        super.checkCompatibility()
        Exceptions.wrap {
            this.node.isNodeType(NodeType.NT_FILE) ||
                    throw InternalStateException("Node is not nt:file type.")
        }
    }


    @Throws(WorkspaceException::class)
    override fun expand() = JcrDetailedDocument.of(this.node)

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrDocument(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, parentId: String, name: String) =
                Exceptions.wrap {
            val parent = session.getNodeByIdentifier(parentId)
            if (!parent.isNodeType(TypeNamespace.FOLDER)) {
                throw CompatibilityException("Parent is not a folder")
            }
            if (parent.hasNode(name)) {
                throw FileExistsException(name)
            }
            val node = parent.addNode(name, TypeNamespace.DOCUMENT)
            node.addMixin(TypeNamespace.FILE)
            node.addNode(Property.JCR_CONTENT, NodeType.NT_RESOURCE)
            JcrDocument.of(node)
        }
    }
}
