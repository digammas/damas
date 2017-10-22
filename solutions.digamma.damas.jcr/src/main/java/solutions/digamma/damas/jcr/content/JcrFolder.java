package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.InternalStateException;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.common.Exceptions;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    JcrFolder(@NotNull Node node) throws WorkspaceException {
        super(node);
    }

    @Override
    protected void checkCompatibility() throws InternalStateException {
        super.checkCompatibility();
        this.checkTypeCompatibility(TypeNamespace.FOLDER);
    }

    @Override
    public @NotNull DetailedFolder expand() throws WorkspaceException {
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
    public Content getContent() throws WorkspaceException {
        if (this.contentDepth == 0) {
            return this.content = null;
        }
        if (this.content != null) {
            return this.content;
        }
        List<JcrDocument> documents = new ArrayList<>();
        List<JcrFolder> folders = new ArrayList<>();
        try {
            NodeIterator iterator = this.node.getNodes();
            while (iterator.hasNext()) {
                Node node = iterator.nextNode();
                if (node.isNodeType(TypeNamespace.DOCUMENT)) {
                    documents.add(new JcrDocument(node));
                } else if (node.isNodeType(TypeNamespace.FOLDER)) {
                    JcrFolder folder = new JcrFolder(node);
                    folder.expandContent(this.contentDepth - 1);
                    folders.add(folder);
                }
            }
        } catch (RepositoryException e) {
            throw Exceptions.convert(e);
        }
        return this.content = new Content() {
            @Override
            public @NotNull List<JcrFolder> getFolders() {
                return Collections.unmodifiableList(folders);
            }

            @Override
            public @NotNull List<JcrDocument> getDocuments() {
                return Collections.unmodifiableList(documents);
            }
        };
    }
}
