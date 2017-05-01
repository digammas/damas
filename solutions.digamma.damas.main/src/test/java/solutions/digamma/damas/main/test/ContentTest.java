package solutions.digamma.damas.main.test;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.Page;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.*;
import solutions.digamma.damas.inspection.Nonnull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

/**
 * Test content managers.
 *
 * @author Ahmad Shahwan
 */
public class ContentTest {

    private LoginManager loginMgr;
    private DocumentManager documentMgr;
    private FolderManager folderMgr;

    private Token adminToken;
    private Folder rootFolder;

    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("repository/cdn/"));
        this.extractResource("/repository/repository.json", "repository/repository.json");
        this.extractResource("/repository/cdn/damas.cdn", "repository/cdn/damas.cdn");
    }

    public void tearDown() throws IOException {
        Files.walk(Paths.get("repository"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private void extractResource(String resourcePath, String distPath)
            throws IOException {
        try (InputStream stream = this
                .getClass()
                .getResourceAsStream(resourcePath)) {
            Files.copy(
                    stream,
                    Paths.get(distPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
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
            public void setName(@Nonnull String value) throws DocumentException {

            }

            @Override
            public Folder getParent() throws DocumentException {
                return null;
            }

            @Override
            public void setParent(@Nonnull Folder value) throws DocumentException {

            }

            @Override
            public String getParentId() throws DocumentException {
                return parentId;
            }

            @Override
            public void setParentId(String value) throws DocumentException {

            }

            @Override
            public @Nonnull DetailedDocument expand() {
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

    public void doTestContent() throws DocumentException {
        try (WeldContainer container = new Weld()
                .initialize()) {
            this.init(container);
            this.adminToken = loginMgr.login("admin", "admin");
            assert this.adminToken != null;
            Page<Folder> folderResult = this.folderMgr.find(this.adminToken);
            this.rootFolder = folderResult.getObjects().get(0);
            final String testFileName = "test.txt";
            Document doc;
            doc = this.createDocument(
                    this.adminToken,
                    this.rootFolder.getId(),
                    testFileName
            );
            assert doc != null;
            String docId = doc.getId();
            doc = this.documentMgr.retrieve(this.adminToken, docId);
            assert testFileName.equals(doc.getName());
            this.documentMgr.delete(this.adminToken, docId);
            try {
                this.documentMgr.retrieve(this.adminToken, docId);
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
