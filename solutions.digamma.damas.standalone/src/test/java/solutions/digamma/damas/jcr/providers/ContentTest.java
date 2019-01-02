package solutions.digamma.damas.jcr.providers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.login.LoginManager;
import solutions.digamma.damas.login.Token;
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
        } catch (WorkspaceException e) {}
        return document;
    }

    private Document createDocument(Token token, String parentId, String name)
        throws WorkspaceException {
        return this.documentMgr.create(
                token,
                this.newDocument(
                        parentId,
                        name
                )
        );
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