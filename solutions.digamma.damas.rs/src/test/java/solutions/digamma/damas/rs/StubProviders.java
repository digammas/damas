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

    abstract static class MockFile implements File {

        @Override
        public @Nullable String getName() throws DocumentException {
            return null;
        }

        @Override
        public void setName(@Nonnull String value) throws DocumentException {
        }

        @Override
        public @Nullable Folder getParent() throws DocumentException {
            return null;
        }

        @Override
        public void setParent(@Nonnull Folder value) throws DocumentException {

        }

        @Override
        public String getParentId() throws DocumentException {
            return null;
        }

        @Override
        public void setParentId(String value) throws DocumentException {

        }

        @Override
        public @Nonnull DetailedFile expand() throws DocumentException {
            return null;
        }
    }

    static class MockDocument extends MockFile implements Document {

        @Override
        public @Nullable String getId() throws DocumentException {
            return DOCUMENT_ID;
        }

        @Override
        public @Nonnull DetailedDocument expand() throws DocumentException {
            return null;
        }
    }

    static class MockFolder extends MockFile implements Folder {

        @Override
        public @Nullable String getId() throws DocumentException {
            return FOLDER_ID;
        }

        @Override
        public void expandContent(long depth) {

        }

        @Override
        public void expandContent() {

        }

        @Override
        public @Nonnull DetailedFolder expand() throws DocumentException {
            return null;
        }
    }

    private Token token = new Token() {
        @Override public String toString() {
            return TOKEN;
        }
    };

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
