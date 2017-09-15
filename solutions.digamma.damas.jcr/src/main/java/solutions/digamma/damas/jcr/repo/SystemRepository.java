package solutions.digamma.damas.jcr.repo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * System-wide repository functionality.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class SystemRepository {

    private Repository repository;

    private Session superuser;
    private Session readonly;

    @Inject
    public SystemRepository(Repository repository) {
        this.repository = repository;
    }

    private Credentials SUPERUSER = new SimpleCredentials(
            "admin",
            "admin".toCharArray()
    );

    private Credentials READONLY = new SimpleCredentials(
            "admin",
            "admin".toCharArray()
    );


    /**
     * Retrieve a valid superuser session.
     *
     * @return JCR superuser session
     * @throws RepositoryException
     */
    public Session getSuperuserSession() throws RepositoryException {
        if (this.superuser == null || !this.superuser.isLive()) {
            this.superuser = this.repository.login(SUPERUSER);
        }
        return this.superuser;
    }

    /**
     * Retrieve a valid readonly session.
     * @return JCR readonly session
     * @throws RepositoryException
     */
    public Session getReadonlySession() throws RepositoryException {
        if (this.readonly == null || !this.readonly.isLive()) {
            this.readonly = this.repository.login(READONLY);
        }
        return this.readonly;
    }
}
