package solutions.digamma.damas.rs;

import java.util.Date;
import org.mockito.Mockito;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.session.Connection;
import solutions.digamma.damas.session.ConnectionManager;
import solutions.digamma.damas.session.LoginManager;
import solutions.digamma.damas.rs.content.CommentSerialization;
import solutions.digamma.damas.rs.content.DocumentSerialization;
import solutions.digamma.damas.rs.content.FolderSerialization;
import solutions.digamma.damas.rs.content.MetadataSerialization;
import solutions.digamma.damas.rs.user.GroupSerialization;
import solutions.digamma.damas.rs.user.UserSerialization;
import solutions.digamma.damas.session.UserToken;
import solutions.digamma.damas.user.GroupManager;
import solutions.digamma.damas.user.UserManager;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
public class StubProviders {

    static final String DOCUMENT_ID = UUID.randomUUID().toString();
    static final String FOLDER_ID = UUID.randomUUID().toString();
    static final String COMMENT_ID = UUID.randomUUID().toString();
    static final String USER_ID = UUID.randomUUID().toString();
    static final String GROUP_ID = UUID.randomUUID().toString();
    static final String TOKEN = UUID.randomUUID().toString();

    private DocumentSerialization document = new DocumentSerialization();
    private FolderSerialization folder = new FolderSerialization();
    private CommentSerialization comment = new CommentSerialization();
    private UserSerialization user = new UserSerialization();
    private GroupSerialization group = new GroupSerialization();


    @Inject
    private Logger log;

    public StubProviders() {
        document.setId(DOCUMENT_ID);
        document.setMetadata(new MetadataSerialization());
        folder.setId(FOLDER_ID);
        comment.setId(COMMENT_ID);

    }

    @Produces @Singleton
    public LoginManager getLoginManager() throws WorkspaceException {
        this.log.info("Acquiring mock login manager.");
        LoginManager manager = Mockito.mock(LoginManager.class);
        Mockito
            .when(manager.login(Mockito.any(), Mockito.any()))
            .thenReturn(new MockToken());
        return manager;
    }

    @Produces @Singleton
    public ConnectionManager getAuthenticationManager()
            throws WorkspaceException {
        this.log.info("Acquiring mock authentication manager.");
        ConnectionManager manager = Mockito.mock(ConnectionManager.class);
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(manager.connect(Mockito.any()))
                .thenReturn(connection);
        return manager;
    }

    @Produces @Singleton
    public DocumentManager getDocumentManager() throws WorkspaceException {
        return mock(DocumentManager.class, document, DOCUMENT_ID);
    }

    @Produces @Singleton
    public FolderManager getFolderManager() throws WorkspaceException {
        return mock(FolderManager.class, folder, FOLDER_ID);
    }

    @Produces @Singleton
    public CommentManager getCommentManager() throws WorkspaceException {
        return mock(CommentManager.class, this.comment, COMMENT_ID);
    }

    @Produces @Singleton
    public UserManager getUserManager() throws WorkspaceException {
        return mock(UserManager.class, this.user, USER_ID);
    }

    @Produces @Singleton
    public GroupManager getGroupManager() throws WorkspaceException {
        return mock(GroupManager.class, this.group, GROUP_ID);
    }

    private <E extends Entity, M extends CrudManager<E>> M mock(
            Class<M> klass, E entity, String id)
            throws WorkspaceException {
        this.log.info(String.format("Acquiring mock %s manager.", klass.getSimpleName()));
        M manager = Mockito.mock(klass);
        Mockito.when(manager.retrieve(Mockito.eq(id)))
                .thenReturn(entity);
        Mockito.when(manager.create(Mockito.any()))
                .thenReturn(entity);
        Mockito.when(manager.update(Mockito.eq(id), Mockito.any()))
                .thenReturn(entity);
        return manager;
    }

    public static class MockToken implements UserToken {

        @Override
        public String getSecret() {
            return TOKEN;
        }

        @Override
        public String getUserLogin() {
            return null;
        }

        @Override
        public Date getCreationDate() {
            return null;
        }

        @Override
        public Date getExpirationDate() {
            return null;
        }
    }
}
