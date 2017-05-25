package solutions.digamma.damas.jcr.providers;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Rule;
import org.junit.Test;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.DetailedDocument;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.inspection.NotNull;

import java.io.IOException;

/**
 * Test content managers.
 *
 * @author Ahmad Shahwan
 */
public class ContentTest {

    @Rule
    public final RepositoryResource jcrResource = new RepositoryResource();

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

    Document newDocument(String parentId, String name) {
        return new Document() {

            @Override
            public String getName() throws DocumentException {
                return name;
            }

            @Override
            public void setName(@NotNull String value) throws DocumentException {

            }

            @Override
            public Folder getParent() throws DocumentException {
                return null;
            }

            @Override
            public void setParent(@NotNull Folder value) throws DocumentException {

            }

            @Override
            public String getParentId() throws DocumentException {
                return parentId;
            }

            @Override
            public void setParentId(String value) throws DocumentException {

            }

            @Override
            public @NotNull DetailedDocument expand() {
                return null;
            }

            @Override
            public String getId() throws DocumentException {
                return null;
            }
        };
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
}
