package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.CompatibilityException;
import solutions.digamma.damas.WorkspaceException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.Namespace;
import solutions.digamma.damas.jcr.error.JcrException;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.model.JcrPathFinder;
import solutions.digamma.damas.jcr.session.SessionWrapper;
import solutions.digamma.damas.logging.Logged;

import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.io.InputStream;

/**
 * JCR implementation of folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrDocumentManager
        extends JcrCrudManager<Document>
        implements JcrPathFinder<Document>, DocumentManager {

    @Logged
    @Override
    public @NotNull Document create(
            @NotNull Token token,
            @NotNull Document entity,
            @NotNull InputStream stream)
            throws WorkspaceException {
        try (SessionWrapper session = this.openSession(token)) {
            JcrDocument document = this.create(session.getSession(), entity);
            document.updateContent(stream);
            return document;
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Logged
    @Override
    public DocumentPayload download(Token token, @NotNull String id)
            throws WorkspaceException {
        try (SessionWrapper session = this.openSession(token)) {
            JcrDocument document = this.retrieve(session.getSession(), id);
            return document.getContent();
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Logged
    @Override
    public void upload(
            @NotNull Token token,
            @NotNull String id,
            @NotNull InputStream stream)
            throws WorkspaceException {
        try (SessionWrapper session = this.openSession(token)) {
            this.retrieve(session.getSession(), id).updateContent(stream);
        } catch (RepositoryException e) {
            throw JcrException.of(e);
        }
    }

    @Override
    protected JcrDocument retrieve(
            @NotNull Session session,
            @NotNull String id)
            throws RepositoryException, WorkspaceException {
        return new JcrDocument(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrDocument create(
            @NotNull Session session,
            @NotNull Document entity)
            throws RepositoryException, WorkspaceException {
        String name = entity.getName();
        Node parent = session.getNodeByIdentifier(entity.getParentId());
        if (!parent.isNodeType(Namespace.FOLDER)) {
            throw new CompatibilityException("Parent is not a folder");
        }
        Node node = parent.addNode(name, Namespace.DOCUMENT);
        node.addMixin(Namespace.FILE);
        node.addNode(Property.JCR_CONTENT, NodeType.NT_RESOURCE);
        return new JcrDocument(node);
    }

    @Override
    protected JcrDocument update(
            @NotNull Session session,
            @NotNull String id,
            @NotNull Document entity)
            throws RepositoryException, WorkspaceException {
        JcrDocument document = this.retrieve(session, id);
        document.update(entity);
        return document;
    }

    @Override
    protected void delete(@NotNull Session session, @NotNull String id)
            throws RepositoryException, WorkspaceException {
        JcrDocument document = this.retrieve(session, id);
        document.remove();
    }

    @Override
    public Document find(Session session, String path)
            throws RepositoryException, WorkspaceException {
        return new JcrDocument(
                session.getNode(JcrFile.CONTENT_ROOT).getNode(path));
    }
}
