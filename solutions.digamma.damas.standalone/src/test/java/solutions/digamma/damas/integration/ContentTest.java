package solutions.digamma.damas.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void testRenameFolder() {
        Map answer;
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
    public void testMoveFolder() {
        Map answer;
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
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        Object id3 = ((Map) ((List) ((Map) answer
                .get("content"))
                .get("folders"))
                .get(0))
                .get("id");
        assert id2.equals(id3);
        assert target
                .path("folders")
                .path(id1)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testRenameDocument() {
        Map answer;
        Map<String, Object> body = new HashMap<>();
        String name = "test_document.txt";
        body.put("parentId", this.rootId);
        body.put("name", name);
        answer = target
                .path("documents")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        assert id != null;
        answer = target
                .path("documents")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        name = "document_test.txt";
        body.clear();
        body.put("name", name);
        assert target
                .path("documents")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2;
        answer = target
                .path("documents")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        assert target
                .path("documents")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void uploadDocument() {
        String id = null;
        try {
            Map answer;
            Map<String, Object> body = new HashMap<>();
            String name = "test_doc.txt";
            body.put("parentId", this.rootId);
            body.put("name", name);
            answer = target
                    .path("documents")
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .post(entity(body))
                    .readEntity(Map.class);
            id = (String) answer.get("id");
            assert id != null;
            String text = "Hello";
            ByteArrayInputStream toUpload = new ByteArrayInputStream(
                    text.getBytes(StandardCharsets.UTF_8));
            assert target
                    .path("documents")
                    .path(id)
                    .path("upload")
                    .request()
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .put(entity(toUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .getStatus() / 100 == 2;
        } finally {
            assert id == null || target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .delete()
                    .getStatus() / 100 == 2;
        }
    }

    @Test
    public void downloadDocument() {
        String id = null;
        try {
            Map answer;
            Map<String, Object> body = new HashMap<>();
            String name = "test_doc.txt";
            body.put("parentId", this.rootId);
            body.put("name", name);
            answer = target
                    .path("documents")
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .post(entity(body))
                    .readEntity(Map.class);
            id = (String) answer.get("id");
            assert id != null;
            String text = "Hello";
            ByteArrayInputStream toUpload = new ByteArrayInputStream(
                    text.getBytes(StandardCharsets.UTF_8));
            target
                    .path("documents")
                    .path(id)
                    .path("upload")
                    .request()
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .put(entity(toUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE))
                    .getStatus();
            InputStream downloaded = target
                    .path("documents")
                    .path(id)
                    .path("download")
                    .request()
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .get()
                    .readEntity(InputStream.class);
            assert text.equals(new BufferedReader(new InputStreamReader(downloaded))
                    .lines()
                    .collect(Collectors.joining("\n")));
        } finally {
            assert id == null || target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .delete()
                    .getStatus() / 100 == 2;
        }
    }

    @Test
    public void testUpdateDocument() {
        String id = null;
        try {
            Map answer;
            Map<String, Object> body = new HashMap<>();
            String mtTextPlain = "text/plain";
            String mtTextHtml = "text/html";
            body.put("parentId", this.rootId);
            body.put("name", "test_document.txt");
            body.put("mimeType", mtTextPlain);
            answer = target
                    .path("documents")
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .post(entity(body))
                    .readEntity(Map.class);
            id = (String) answer.get("id");
            assert id != null;
            answer = target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .get()
                    .readEntity(Map.class);
            assert mtTextPlain.equals(answer.get("mimeType"));
            body.clear();
            body.put("mimeType", mtTextHtml);
            assert target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .put(entity(body))
                    .getStatus() / 100 == 2;
            answer = target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .get()
                    .readEntity(Map.class);
            assert mtTextHtml.equals(answer.get("mimeType"));
        } finally {
            assert id == null || target
                    .path("documents")
                    .path(id)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .delete()
                    .getStatus() / 100 == 2;
        }
    }
}
