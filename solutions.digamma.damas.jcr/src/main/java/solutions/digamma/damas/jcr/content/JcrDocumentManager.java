package solutions.digamma.damas.jcr.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.jcr.auth.UserSession;
import solutions.digamma.damas.jcr.model.JcrCrudManager;

import javax.jcr.RepositoryException;

/**
 * JCR implementation of folder manager.
 *
 * @author Ahmad Shahwan
 */
public class JcrDocumentManager
        extends JcrCrudManager<Document> implements DocumentManager {

    @Override
    protected JcrDocument retrieve(
            @Nonnull UserSession session,
            @Nonnull String id)
            throws RepositoryException, DocumentException {
        return new JcrDocument(session.use().getNodeByIdentifier(id));
    }

    @Override
    public JcrDocument create(
            @Nonnull UserSession session,
            @Nonnull Document entity)
            throws RepositoryException, DocumentException {
        return JcrDocument.create(
                entity.getName(),
                session.use().getNodeByIdentifier(entity.getParentId())
        );
    }

    @Override
    public JcrDocument update(
            @Nonnull UserSession session,
            @Nonnull String id,
            @Nonnull Document entity)
            throws RepositoryException, DocumentException {
        JcrDocument document = this.retrieve(session, id);
        document.update(entity);
        return document;
    }

    @Override
    public void delete(@Nonnull UserSession session, @Nonnull String id)
            throws RepositoryException, DocumentException {
        JcrDocument document = this.retrieve(session, id);
        document.remove();
    }

}
