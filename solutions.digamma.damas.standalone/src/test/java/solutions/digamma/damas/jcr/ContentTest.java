package solutions.digamma.damas.jcr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.session.Transaction;
import solutions.digamma.damas.session.TransactionManager;
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
    private TransactionManager transactionMgr;

    @Inject
    private DocumentManager documentMgr;

    @Inject
    private FolderManager folderMgr;

    private Document newDocument(String parentId, String name) {
    Document document = Mockito.mock(Document.class);
        Mockito.when(document.getParentId()).thenReturn(parentId);
        Mockito.when(document.getName()).thenReturn(name);
        return document;
    }

    private Document createDocument(String parentId, String name)
        throws WorkspaceException {
        return this.documentMgr.create(this.newDocument(parentId,name));
    }

    @Test
    public void testContent() throws WorkspaceException {
        Token adminToken = loginMgr.login("admin", "admin");
        assert adminToken != null;
        try (Transaction transaction = transactionMgr.begin(adminToken)) {
            assert transaction != null;
            Folder rootFolder = this.folderMgr.find("/");
            final String testFileName = "test.txt";
            Document doc;
            doc = this.createDocument(
                    rootFolder.getId(),
                    testFileName
            );
            assert doc != null;
            String docId = doc.getId();
            doc = this.documentMgr.retrieve(docId);
            assert testFileName.equals(doc.getName());
            this.documentMgr.delete(docId);
            try {
                this.documentMgr.retrieve(docId);
                assert false;
            } catch (NotFoundException e) {
                assert true;
            }
        }
    }
}
