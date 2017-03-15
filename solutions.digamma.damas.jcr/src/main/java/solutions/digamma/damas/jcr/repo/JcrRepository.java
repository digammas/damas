package solutions.digamma.damas.jcr.repo;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.*;

/**
 * JCR-specific repository-wide functionality.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrRepository {

    @Inject
    private Repository repository;

    private Session superuser;
    private Session readonly;

    private Credentials SUPERUSER = new SimpleCredentials(
            "admin",
            "admin".toCharArray()
    );

    private Credentials READONLY = new SimpleCredentials(
            "admin",
            "admin".toCharArray()
    );

    public Session getSuperuserSession() throws RepositoryException {
        if (this.superuser == null || !this.superuser.isLive()) {
            this.superuser = this.repository.login(SUPERUSER);
        }
        return this.superuser;
    }

    public Session getReadonlySession() throws RepositoryException {
        if (this.readonly == null || !this.readonly.isLive()) {
            this.readonly = this.repository.login(READONLY);
        }
        return this.readonly;
    }
}
