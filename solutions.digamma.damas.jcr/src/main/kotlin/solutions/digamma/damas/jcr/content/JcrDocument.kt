package solutions.digamma.damas.jcr.content

import org.apache.tika.mime.MimeType
import solutions.digamma.damas.common.CompatibilityException
import solutions.digamma.damas.common.WorkspaceException
import solutions.digamma.damas.common.InternalStateException
import solutions.digamma.damas.content.Document
import solutions.digamma.damas.content.DocumentPayload
import solutions.digamma.damas.content.File
import solutions.digamma.damas.content.Version
import solutions.digamma.damas.jcr.common.Exceptions
import solutions.digamma.damas.jcr.names.TypeNamespace

import javax.jcr.Node
import javax.jcr.Property
import javax.jcr.nodetype.NodeType
import java.io.InputStream
import java.util.ArrayList
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
protected constructor(node: Node) : JcrFile(node),
        JcrCommentReceiver, Document {

    val content: DocumentPayload
    get() = Exceptions.uncheck {
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

    override fun getVersions(): List<Version> {
        return ArrayList(0)
    }

    @Throws(WorkspaceException::class)
    internal fun updateContent(stream: InputStream) {
        Exceptions.check {
            val binary = this.session.valueFactory.createBinary(stream)
            this.node
                    .getNode(Property.JCR_CONTENT)
                    .setProperty(Property.JCR_DATA, binary)
        }
    }

    @Throws(WorkspaceException::class)
    override fun getMimeType(): String? = Exceptions.check {
        var content = this.node.getNode(Node.JCR_CONTENT)
        if (content.hasProperty(Property.JCR_MIMETYPE)) {
            content.getProperty(Property.JCR_MIMETYPE).string
        } else null
    }

    @Throws(WorkspaceException::class)
    override fun setMimeType(value: String?) {
        Exceptions.check {
            this.node
                    .getNode(Node.JCR_CONTENT)
                    .setProperty(Property.JCR_MIMETYPE, value)
        }
    }

    @Throws(WorkspaceException::class)
    fun update(other: Document) {
        super.update(other)
        other.mimeType?.let { this.mimeType = it }
    }

    @Throws(InternalStateException::class)
    override fun checkCompatibility() {
        super.checkCompatibility()
        Exceptions.check {
            this.node.isNodeType(NodeType.NT_FILE) ||
                    throw InternalStateException("Node is not nt:file type.")
        }
    }

    companion object {

        @Throws(WorkspaceException::class)
        fun of(node: Node) = JcrDocument(node)

        @Throws(WorkspaceException::class)
        fun from(session: Session, parentId: String, name: String) =
                Exceptions.check {
            val parent = session.getNodeByIdentifier(parentId)
            if (!parent.isNodeType(TypeNamespace.FOLDER)) {
                throw CompatibilityException("Parent is not a folder")
            }
            if (parent.hasNode(name)) {
                throw FileExistsException(name)
            }
            val node = parent.addNode(name, TypeNamespace.DOCUMENT)
            node.addMixin(TypeNamespace.FILE)
            node.addNode(Property.JCR_CONTENT, NodeType.NT_RESOURCE).apply {
                setProperty(Property.JCR_DATA, "")
            }
            JcrDocument.of(node).apply { initPermissions() }
        }
    }
}
