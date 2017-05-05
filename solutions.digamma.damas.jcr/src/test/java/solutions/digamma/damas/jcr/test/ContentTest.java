package solutions.digamma.damas.jcr.test;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.mockito.Mockito;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;

import java.io.IOException;

/**
 * Test content managers.
 *
 * @author Ahmad Shahwan
 */
public class ContentTest {

    private LoginManager loginMgr;
    private DocumentManager documentMgr;
    private FolderManager folderMgr;

    public void setUp() throws IOException {
    }

    public void tearDown() throws IOException {
    }

    private void init(WeldContainer container) {
        this.loginMgr = container.select(LoginManager.class).get();
        this.documentMgr = container.select(DocumentManager.class).get();
        this.folderMgr = container.select(FolderManager.class).get();
    }

    private Document createDocument(Token token, String parentId, String name)
        throws DocumentException {
        Document document = Mockito.mock(Document.class);
        Mockito.when(document.getParentId()).thenReturn(parentId);
        Mockito.when(document.getName()).thenReturn(name);
        return this.documentMgr.create(token, document);
    }

    private void doTestContent() throws DocumentException {
        try (WeldContainer container = new Weld()
                .initialize()) {
            this.init(container);
            Token adminToken = loginMgr.login("admin", "admin");
            assert adminToken != null;
            Page<Folder> folderResult = this.folderMgr.find(adminToken);
            Folder rootFolder = folderResult.getObjects().get(0);
            final String testFileName = "test.txt";
            Document doc;
            doc = this.createDocument(
                    adminToken,
                    rootFolder.getId(),
                    testFileName
            );
            assert doc != null;
            String docId = doc.getId();
            doc = this.documentMgr.retrieve(adminToken, docId);
            assert testFileName.equals(doc.getName());
            this.documentMgr.delete(adminToken, docId);
            try {
                this.documentMgr.retrieve(adminToken, docId);
                assert false;
            } catch (NotFoundException e) {
                assert true;
            }
        }
    }

    public void testContent() throws Exception {
        try {
            this.setUp();
            this.doTestContent();
        } finally {
            this.tearDown();
        }
    }

    public static void main(String[] argv) throws Exception {
        new ContentTest().testContent();
    }
}
