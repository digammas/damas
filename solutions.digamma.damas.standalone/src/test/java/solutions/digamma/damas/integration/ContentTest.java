package solutions.digamma.damas.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Content integration test.
 *
 * @author Ahmad Shahwan
 */
@RunWith(ContainerRunner.class)
@Singleton
public class ContentTest extends IntegrationTest {

    @Test
    public void testBadAuth() {
        Map<String, Object> login = new HashMap<>(2);
        login.put("username", "villain");
        login.put("password", "bad");
        int status = target
                .path("login")
                .request(MEDIA_TYPE)
                .post(entity(login))
                .getStatus();
        assert status == 401;
    }

    @Test
    public void testUnauthorizedAccess() {
        int status = target
                .path("folders")
                .request(MEDIA_TYPE)
                .get()
                .getStatus();
        assert status == 401;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRenameFolder() {
        Map<String, Object> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "test_folder";
        body.put("parentId", this.rootId);
        body.put("name", name);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        assert id != null;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        name = "folder_test";
        body.clear();
        body.put("name", name);
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMoveFolder() {
        Map<String, Object> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "test_folder";
        body.put("parentId", this.rootId);
        body.put("name", name);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        assert id != null;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        body.clear();
        body.put("name", "temp");
        body.put("parentId", this.rootId);
        String tempId = (String) target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class).get("id");
        body.clear();
        body.put("parentId", tempId);
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert tempId.equals(answer.get("parentId"));
        assert target
                .path("folders")
                .path(tempId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testRetrieveFolderByPath() {
        Map answer;
        Map<String, Object> body = new HashMap<>();
        String name1 = "test_folder";
        String name2 = "test_subfolder";
        body.put("parentId", this.rootId);
        body.put("name", name1);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id1 = (String) answer.get("id");
        body.put("parentId", id1);
        body.put("name", name2);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id2 = (String) answer.get("id");
        String path = String.format("folders/at/%s/%s", name1, name2);
        answer = target
                .path(path)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert id2.equals(answer.get("id"));
        assert target
                .path("folders")
                .path(id2)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
        assert target
                .path("folders")
                .path(id1)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testRetrieveFoldersAtDepth() {
        Map answer;
        Map<String, Object> body = new HashMap<>();
        String name1 = "test_folder";
        String name2 = "test_subfolder";
        body.put("parentId", this.rootId);
        body.put("name", name1);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id1 = (String) answer.get("id");
        body.put("parentId", id1);
        body.put("name", name2);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id2 = (String) answer.get("id");
        answer = target
                .path("folders")
                .path(id1)
                .queryParam("depth", 1)
                .queryParam("full", true)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert id2.equals(answer.get("id"));
        assert target
                .path("folders")
                .path(id2)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
        assert target
                .path("folders")
                .path(id1)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }
}
