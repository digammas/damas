package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.ReadManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.jcr.auth.UserSession;
import solutions.digamma.damas.jcr.fail.JcrException;

import javax.jcr.RepositoryException;

/**
 * Abstract read manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrReadManager<T extends Entity>
        extends JcrManager implements ReadManager<T> {

    @Override
    public T retrieve(@Nonnull Token token, @Nonnull String id)
            throws DocumentException {
        this.logger.info(String.format(
                "%s: retrieve object with ID %s.",
                this.getClass().getName(),
                id)
        );
        try (UserSession session = getSession(token).open()) {
            return this.retrieve(session, id);
        } catch (RepositoryException e) {
            this.logger.severe(String.format(
                    "%s: %s",
                    this.getClass().getName(),
                    e.getMessage())
            );
            throw JcrException.wrap(e);
        } catch (DocumentException e) {
            this.logger.severe(String.format(
                    "%s: %s",
                    this.getClass().getName(),
                    e.getMessage())
            );
            throw e;
        }
    }

    /**
     * Perform retrieval.
     *
     * @param session
     * @param id
     * @return
     * @throws RepositoryException
     * @throws DocumentException
     */
    abstract protected T retrieve(@Nonnull UserSession session, @Nonnull String id)
            throws RepositoryException, DocumentException;
}
