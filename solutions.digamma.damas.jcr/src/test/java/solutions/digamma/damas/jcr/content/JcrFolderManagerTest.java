package solutions.digamma.damas.jcr.content;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import solutions.digamma.damas.NotFoundException;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.jcr.Mocks;
import solutions.digamma.damas.jcr.WeldTest;

/**
 * Created by ahmad on 9/3/17.
 */
public class JcrFolderManagerTest extends WeldTest {

    private FolderManager manager;
    private Token token;

    @Before
    public void setUp() throws Exception {
        this.manager = inject(JcrFolderManager.class);
        this.token = this.login.login("admin", "admin");
    }

    @After
    public void tearDown() throws Exception {
        this.login.logout(this.token);
    }

    @Test
    public void retrieve() throws Exception {
        final String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        String id =
                manager.create(this.token, Mocks.folder(rootId, name)).getId();
        Folder folder = manager.retrieve(this.token, id);
        assert name.equals(folder.getName()) : "Folder name mismatch";
        assert rootId.equals(folder.getParentId()) : "Folder ID mismatch";
    }

    @Test
    public void create() throws Exception {
        final String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        Folder folder =
                manager.create(this.token, Mocks.folder(rootId, name));
        assert name.equals(folder.getName()) : "Folder name mismatch";
        assert rootId.equals(folder.getParentId()) : "Folder ID mismatch";
    }

    @Test
    public void updateName() throws Exception {
        String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        String id =
                manager.create(this.token, Mocks.folder(rootId, name)).getId();
        name = "new.txt";
        manager.update(this.token, id, Mocks.folder(null, name));
        Folder folder = manager.retrieve(this.token, id);
        assert id.equals(folder.getId()) : "Updated folder ID mismatch.";
        assert name.equals(folder.getName()) :
                "Updated folder name mismatch.";
        assert rootId.equals(folder.getParentId()) :
                "Updated folder parent ID mismatch.";
    }

    @Test
    public void updateParent() throws Exception {
        String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        String id =
                manager.create(this.token, Mocks.folder(rootId, name)).getId();
        String parentId = manager
                .create(this.token, Mocks.folder(rootId, "parent"))
                .getId();
        manager.update(this.token, id, Mocks.folder(parentId, null));
        Folder folder = manager.retrieve(this.token, id);
        assert id.equals(folder.getId()) : "Updated folder ID mismatch.";
        assert name.equals(folder.getName()) :
                "Updated folder name mismatch.";
        assert parentId.equals(folder.getParentId()) :
                "Updated folder parent ID mismatch.";
    }

    @Test
    public void updateNameAndParent() throws Exception {
        String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        String id =
                manager.create(this.token, Mocks.folder(rootId, name)).getId();
        String parentId = manager
                .create(this.token, Mocks.folder(rootId, "parent"))
                .getId();
        name = "new";
        manager.update(this.token, id, Mocks.folder(parentId, name));
        Folder folder = manager.retrieve(this.token, id);
        assert id.equals(folder.getId()) : "Updated folder ID mismatch.";
        assert name.equals(folder.getName()) :
                "Updated folder name mismatch.";
        assert parentId.equals(folder.getParentId()) :
                "Updated folder parent ID mismatch.";
    }


    @Test
    public void delete() throws Exception {
        String name = "test";
        String rootId = this.manager
                .find(this.token).getObjects().iterator().next().getId();
        String id =
                manager.create(this.token, Mocks.folder(rootId, name)).getId();
        manager.delete(this.token, id);
        try {
            manager.retrieve(this.token, id);
            assert false : "NotFoundException should be thrown.";
        } catch (NotFoundException e) {}
    }

    @Test
    public void find() throws Exception {
    }
}