package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.JcrException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * JCR-based folder implementation.
 *
 * @author Ahmad Shahwan
 */
public class JcrFolder extends JcrFile implements Folder {

    /**
     * Content depth used to indicate to which depth is content returned with
     * method {@code getContent()}. If negative, content will be returned down
     * to leaves. If zero, no content is returned at all. Zero is the default
     * value.
     */
    private int contentDepth = 0;

    /**
     * Folder content. If null, the content is not visited yet.
     */
    private Content content;

    /**
     * Construct folder with a JCR node.
     *
     * @param node
     */
    JcrFolder(@NotNull Node node) throws DocumentException {
        super(node);
    }

    /**
     * Construct new folder, given parent node and name.
     *
     * @param name
     * @param parent
     * @return
     * @throws DocumentException
     */
    JcrFolder(@NotNull String name, @NotNull Node parent)
        throws DocumentException {
        this(create(name, parent));
    }

    @Override
    protected void checkCompatibility() throws CompatibilityException {
        super.checkCompatibility();
        this.checkTypeCompatibility(Namespace.FOLDER);
    }

    @Override
    public @NotNull DetailedFolder expand() throws DocumentException {
        return new JcrDetailedFolder(this.node);
    }

    @Override
    public void expandContent(int depth) {
        if (depth != this.contentDepth) {
            this.content = null;
            this.contentDepth = depth;
        }
    }

    @Override
    public void expandContent() {
        if (this.contentDepth >= 0) {
            this.content = null;
            this.contentDepth = -1;
        }
    }

    @Override
    public Content getContent() throws DocumentException {
        if (content != null) {
            return content;
        }
        long depth = this.contentDepth;
        List<JcrDocument> documents = new ArrayList<>();
        List<JcrFolder> folders = new ArrayList<>();
        if (depth != 0) {
            try {
                NodeIterator iterator = this.node.getNodes();
                while (iterator.hasNext()) {
                    Node node = iterator.nextNode();
                    if (node.isNodeType(Namespace.DOCUMENT)) {
                        documents.add(new JcrDocument(node));
                    } else if (node.isNodeType(Namespace.FOLDER)) {
                        JcrFolder folder = new JcrFolder(node);
                        folder.expandContent(this.contentDepth - 1);
                        folders.add(folder);
                    }
                }
            } catch (RepositoryException e) {
                throw JcrException.wrap(e);
            }
        }
        return this.content = new Content() {
            @Override
            public @NotNull List<JcrFolder> getFolders() {
                return folders;
            }

            @Override
            public @NotNull List<JcrDocument> getDocuments() {
                return documents;
            }
        };
    }

    /**
     * Help method to create folder's JCR node.
     *
     * @param name      node name
     * @param parent    parent node
     * @return          JCR node
     * @throws DocumentException
     */
    static protected Node create(
            @NotNull String name,
            @NotNull Node parent)
            throws DocumentException {
        try {
            return JcrFile.create(name, Namespace.FOLDER, parent);
        } catch (RepositoryException e) {
            throw JcrException.wrap(e);
        }
    }

}
