package solutions.digamma.damas.jcr.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solutions.digamma.damas.common.CompatibilityException;
import solutions.digamma.damas.common.NotFoundException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.jcr.Mocks;
import solutions.digamma.damas.jcr.WeldTest;

/**
 * Comment manager test suite.
 *
 * Created by Ahmad on 10/8/17.
 */
public class JcrCommentManagerTest extends WeldTest {

    private JcrCommentManager manager = inject(JcrCommentManager.class);
    private Token token;
    private Folder folder;
    private Document document;

    @Before
    public void setUp() throws Exception {
        FolderManager fm = inject(JcrFolderManager.class);
        DocumentManager dm = inject(DocumentManager.class);
        this.token = this.login.login("admin", "admin");
        String parentId = fm
                .find(this.token).getObjects().iterator().next().getId();
        this.folder = fm.create(this.token, Mocks.folder(parentId, "test"));
        parentId = this.folder.getId();
        this.document = dm
                .create(this.token, Mocks.document(parentId, "file.tst"));
    }

    @After
    public void tearDown() throws Exception {
        this.login.logout(this.token);
    }

    @Test
    public void retrieve() throws Exception {
        final String text = "Hello Comment";
        String id = manager.create(
                this.token,
                Mocks.comment(this.document.getId(), text, 1L)
        ).getId();
        Comment comment = manager.retrieve(this.token, id);
        assert text.equals(comment.getText());
        assert comment.getRank() == 1L;
    }

    @Test
    public void create() throws Exception {
        final String text = "Hello Comment";
        Comment comment = manager.create(
                this.token,
                Mocks.comment(this.document.getId(), text, 1L));
        assert text.equals(comment.getText());
        assert comment.getRank() == 1L;
    }

    @Test(expected = CompatibilityException.class)
    public void createWithError() throws Exception {
        manager.create(
                this.token,
                Mocks.comment(this.folder.getId(), "Hello Comment", 1L));
    }

    @Test
    public void update() throws Exception {
        String text = "Hello Comment";
        String id = manager.create(
                this.token,
                Mocks.comment(this.document.getId(), text, 1L)
        ).getId();
        text = "Salut commentaire";
        manager.update(
                this.token,
                id,
                Mocks.comment(this.document.getId(), text, 0L));
        Comment comment = manager.retrieve(this.token, id);
        assert text.equals(comment.getText());
        assert comment.getRank() == 0L;
    }

    @Test
    public void delete() throws Exception {
        String text = "Hello Comment";
        String id = manager.create(
                this.token,
                Mocks.comment(this.document.getId(), text, 1L)
        ).getId();
        manager.delete(this.token, id);
        try {
            manager.retrieve(this.token, id);
            assert false;
        } catch (NotFoundException e) {}
    }

    @Test
    public void find() throws Exception {
    }
}