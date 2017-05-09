package solutions.digamma.damas.rs;

import org.mockito.Mockito;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.DetailedFile;
import solutions.digamma.damas.content.DetailedFolder;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.Nonnull;
import solutions.digamma.damas.inspection.Nullable;
import solutions.digamma.damas.rs.content.DocumentUpdater;
import solutions.digamma.damas.rs.content.FolderUpdater;

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
    static final String TOKEN = UUID.randomUUID().toString();

    static class MockDocument extends DocumentUpdater {

        @Override
        public @Nullable String getId() {
            return DOCUMENT_ID;
        }
    }

    static class MockFolder extends FolderUpdater {

        @Override
        public @Nullable String getId() {
            return FOLDER_ID;
        }
    }

    private Token token = () -> TOKEN;

    private Document document = new MockDocument();
    private Folder folder = new MockFolder();


    @Inject
    private Logger log;

    public StubProviders() throws DocumentException {
    }

    @Produces @Singleton
    public LoginManager getLoginManager() throws DocumentException {
        this.log.info("Acquiring mock login manager.");
        LoginManager manager = mock(LoginManager.class);
        when(manager.login(any(), any())).thenReturn(token);
        return manager;
    }

    @Produces @Singleton
    public DocumentManager getDocumentManager() throws DocumentException {
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
    public FolderManager getFolderManager() throws DocumentException {
        this.log.info("Acquiring mock folder manager.");
        FolderManager manager = mock(FolderManager.class);
        Mockito.when(manager.retrieve(any(), eq(FOLDER_ID)))
                .thenReturn(this.folder);
        Mockito.when(manager.create(any(), any()))
                .thenReturn(this.folder);
        Mockito.when(manager.update(any(), eq(DOCUMENT_ID), any()))
                .thenReturn(this.folder);
        return manager;
    }
}
