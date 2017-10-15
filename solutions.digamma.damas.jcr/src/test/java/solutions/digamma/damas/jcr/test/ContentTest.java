package solutions.digamma.damas.jcr.test;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.entity.Page;
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

    private static WeldContainer container;

    private LoginManager loginMgr;
    private DocumentManager documentMgr;
    private FolderManager folderMgr;

    @BeforeClass
    public static void setUpClass() {
        container = new Weld().initialize();
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Before
    public void setUp() throws IOException {
        this.loginMgr = container.select(LoginManager.class).get();
        this.documentMgr = container.select(DocumentManager.class).get();
        this.folderMgr = container.select(FolderManager.class).get();
    }

    @After
    public void tearDown() throws IOException {
    }

    private Document createDocument(Token token, String parentId, String name)
        throws WorkspaceException {
        Document document = Mockito.mock(Document.class);
        Mockito.when(document.getParentId()).thenReturn(parentId);
        Mockito.when(document.getName()).thenReturn(name);
        return this.documentMgr.create(token, document);
    }

    @Test
    public void testContent() throws WorkspaceException {
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
