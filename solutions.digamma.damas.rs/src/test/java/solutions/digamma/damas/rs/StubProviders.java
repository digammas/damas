package solutions.digamma.damas.rs;

import org.mockito.Mockito;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.login.LoginManager;
import solutions.digamma.damas.login.Token;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.rs.content.CommentSerialization;
import solutions.digamma.damas.rs.content.DocumentSerialization;
import solutions.digamma.damas.rs.content.FolderSerialization;
import solutions.digamma.damas.rs.content.MetadataSerialization;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
public class StubProviders extends Mockito {

    static final String DOCUMENT_ID = UUID.randomUUID().toString();
    static final String FOLDER_ID = UUID.randomUUID().toString();
    static final String COMMENT_ID = UUID.randomUUID().toString();
    static final String TOKEN = UUID.randomUUID().toString();

    private DocumentSerialization document = new DocumentSerialization();
    private FolderSerialization folder = new FolderSerialization();
    private CommentSerialization comment = new CommentSerialization();


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
        LoginManager manager = mock(LoginManager.class);
        when(manager.login(any(), any())).thenReturn(new MockToken());
        return manager;
    }

    @Produces @Singleton
    public DocumentManager getDocumentManager() throws WorkspaceException {
        this.log.info("Acquiring mock document manager.");
        DocumentManager manager = mock(DocumentManager.class);
        Mockito.when(manager.retrieve(any(), eq(DOCUMENT_ID)))
                .thenReturn(this.document);
        Mockito.when(manager.create(any(), any()))
                .thenReturn(this.document);
        Mockito.when(manager.update(any(), eq(DOCUMENT_ID), any()))
                .thenReturn(this.document);
        return manager;
    }

    @Produces @Singleton
    public FolderManager getFolderManager() throws WorkspaceException {
        this.log.info("Acquiring mock folder manager.");
        FolderManager manager = mock(FolderManager.class);
        Mockito.when(manager.retrieve(any(), eq(FOLDER_ID)))
                .thenReturn(this.folder);
        Mockito.when(manager.create(any(), any()))
                .thenReturn(this.folder);
        Mockito.when(manager.update(any(), eq(FOLDER_ID), any()))
                .thenReturn(this.folder);
        return manager;
    }

    @Produces @Singleton
    public CommentManager getCommentManager() throws WorkspaceException {
        this.log.info("Acquiring mock document manager.");
        CommentManager manager = mock(CommentManager.class);
        Mockito.when(manager.retrieve(any(), eq(COMMENT_ID)))
                .thenReturn(this.comment);
        Mockito.when(manager.create(any(), any()))
                .thenReturn(this.comment);
        Mockito.when(manager.update(any(), eq(COMMENT_ID), any()))
                .thenReturn(this.comment);
        return manager;
    }

    public static class MockToken implements Token {

        @Override
        public String getSecret() {
            return TOKEN;
        }
    }
}
