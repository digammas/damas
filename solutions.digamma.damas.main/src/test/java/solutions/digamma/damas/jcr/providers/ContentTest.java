package solutions.digamma.damas.jcr.providers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.cdi.ContainerRunner;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;

import javax.inject.Inject;

/**
 * Test content managers.
 *
 * @author Ahmad Shahwan
 */
@RunWith(ContainerRunner.class)
public class ContentTest {

    @Inject
    private LoginManager loginMgr;

    @Inject
    private DocumentManager documentMgr;

    @Inject
    private FolderManager folderMgr;

    private Document newDocument(String parentId, String name) {
        Document document = Mockito.mock(Document.class);
        try {
            Mockito.when(document.getParentId()).thenReturn(parentId);
            Mockito.when(document.getName()).thenReturn(name);
        } catch (DocumentException e) {}
        return document;
    }

    private Document createDocument(Token token, String parentId, String name)
        throws DocumentException {
        return this.documentMgr.create(
                token,
                this.newDocument(
                        parentId,
                        name
                )
        );
    }

    @Test
    public void testContent() throws DocumentException {
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
