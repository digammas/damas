package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.common.CompatibilityException;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.common.Exceptions;
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
 * JCR implementation convert folder manager.
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
            throw Exceptions.convert(e);
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
            throw Exceptions.convert(e);
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
            throw Exceptions.convert(e);
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
        if (!parent.isNodeType(TypeNamespace.FOLDER)) {
            throw new CompatibilityException("Parent is not a folder");
        }
        Node node = parent.addNode(name, TypeNamespace.DOCUMENT);
        node.addMixin(TypeNamespace.FILE);
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
                session.getNode(JcrFile.ROOT_PATH).getNode(path));
    }
}
