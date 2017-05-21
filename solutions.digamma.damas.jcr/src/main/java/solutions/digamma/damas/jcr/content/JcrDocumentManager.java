package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.DocumentPayload;
import solutions.digamma.damas.inspection.NotNull;
import solutions.digamma.damas.jcr.error.JcrExceptionMapper;
import solutions.digamma.damas.jcr.model.JcrCrudManager;
import solutions.digamma.damas.jcr.session.UserSession;
import solutions.digamma.damas.logging.Logged;

import javax.inject.Singleton;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.InputStream;

/**
 * JCR implementation of folder manager.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrDocumentManager
        extends JcrCrudManager<Document> implements DocumentManager {

    @Logged
    @Override
    public @NotNull Document create(
            @NotNull Token token,
            @NotNull Document entity,
            @NotNull InputStream stream)
            throws DocumentException {
        try (UserSession session = this.getSession(token).open()) {
            JcrDocument document = this.create(session.toJcrSession(), entity);
            document.updateContent(stream);
            return document;
        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }

    }

    @Logged
    @Override
    public DocumentPayload download(Token token, @NotNull String id) throws DocumentException {
        try (UserSession session = this.getSession(token).open()) {
            JcrDocument document = this.retrieve(session.toJcrSession(), id);
            return document.getContent();

        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Logged
    @Override
    public void upload(
            @NotNull Token token,
            @NotNull String id,
            @NotNull InputStream stream)
            throws DocumentException {
        try (UserSession session = this.getSession(token).open()) {
            this.retrieve(session.toJcrSession(), id).updateContent(stream);

        } catch (RepositoryException e) {
            throw JcrExceptionMapper.map(e);
        }
    }

    @Override
    protected JcrDocument retrieve(
            @NotNull Session session,
            @NotNull String id)
            throws RepositoryException, DocumentException {
        return new JcrDocument(session.getNodeByIdentifier(id));
    }

    @Override
    protected JcrDocument create(
            @NotNull Session session,
            @NotNull Document entity)
            throws RepositoryException, DocumentException {
        JcrDocument document = JcrDocument.create(
                entity.getName(),
                session.getNodeByIdentifier(entity.getParentId())
        );
        return document;
    }

    @Override
    protected JcrDocument update(
            @NotNull Session session,
            @NotNull String id,
            @NotNull Document entity)
            throws RepositoryException, DocumentException {
        JcrDocument document = this.retrieve(session, id);
        document.update(entity);
        return document;
    }

    @Override
    protected void delete(@NotNull Session session, @NotNull String id)
            throws RepositoryException, DocumentException {
        JcrDocument document = this.retrieve(session, id);
        document.remove();
    }
}
