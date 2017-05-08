package solutions.digamma.damas.rs;

import org.mockito.Mockito;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Ahmad Shahwan
 */
public class StubProviders extends Mockito {

    public static final String DOCUMENT_ID = UUID.randomUUID().toString();
    public static final String FOLDER_ID = UUID.randomUUID().toString();
    public static final String TOKEN = UUID.randomUUID().toString();

    private Document document = mock(Document.class);
    private Folder folder = mock(Folder.class);
    private Token token = mock(Token.class);

    @Inject
    private Logger log;

    public StubProviders() throws DocumentException {
        when(document.getId()).thenReturn(DOCUMENT_ID);
        when(folder.getId()).thenReturn(FOLDER_ID);
        when(token.toString()).thenReturn(TOKEN);
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
        Mockito.when(manager.retrieve(any(), DOCUMENT_ID))
                .thenReturn(this.document);
        return manager;
    }

    @Produces @Singleton
    public FolderManager getFolderManager() throws DocumentException {
        this.log.info("Acquiring mock folder manager.");
        FolderManager manager = mock(FolderManager.class);
        Mockito.when(manager.retrieve(any(), FOLDER_ID))
                .thenReturn(this.folder);
        return manager;
    }
}
