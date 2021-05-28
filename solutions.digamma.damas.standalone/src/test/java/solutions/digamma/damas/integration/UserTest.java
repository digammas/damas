package solutions.digamma.damas.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Content integration test.
 *
 * @author Ahmad Shahwan
 */
@RunWith(ContainerRunner.class)
@Singleton
public class UserTest extends IntegrationTest {

    @Test
    public void testCreateRetrieveDeleteUser() {
        Map answer;
        Map<String, Object> body = new HashMap<>();
        String firstName = "Jonh";
        String lastName = "Smith";
        String login = "jsmith";
        body.put("firstName", firstName);
        body.put("lastName", lastName);
        body.put("login", login);
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        answer = target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert answer.get("firstName").equals(firstName);
        assert answer.get("lastName").equals(lastName);
        assert answer.get("login").equals(login);
        assert target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testCreateWithPassword() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String login = "jsmith";
        String password = "P@a55w0rd";
        body.put("login", login);
        body.put("password", password);
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        body.clear();
        body.put("username", login);
        body.put("password", password);
        Map<?, ?> auth = target
                .path("auth")
                .request(MEDIA_TYPE)
                .post(entity(body))
                .readEntity(HashMap.class);
        assert auth.get("secret") instanceof String :
            "Cannot use created password to log in.";
        assert target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testUpdatePassword() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String login = "jsmith";
        String password = "P@a55w0rd";
        body.put("login", login);
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        body.clear();
        body.put("password", password);
        assert  target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2 :
            "Bad response when trying to update password.";
        body.clear();
        body.put("username", login);
        body.put("password", password);
        Map<?, ?> auth = target
                .path("auth")
                .request(MEDIA_TYPE)
                .post(entity(body))
                .readEntity(HashMap.class);
        assert auth.get("secret") instanceof String :
            "Cannot use updated password to log in.";
        assert target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testCreateRetrieveDeleteGroup() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "clerks";
        body.put("name", name);
        answer = target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        if (id == null) {
            System.out.println("ANSWER IS " + Arrays.toString(answer.entrySet().toArray()));
        }
        answer = target
                .path("groups")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert answer.get("name").equals(name);
        assert target
                .path("groups")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testAddUserToGroup() {
        Map<?, ?> answer;
        Map<String, Object> body;
        body = new HashMap<>();
        String clerks = "clerks";
        String managers = "managers";
        List<String> groupIds = new ArrayList<>();
        // Create group clerks
        body.put("name", clerks);
        groupIds.add((String) target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class).get("id"));
        // Create group managers
        body.put("name", managers);
        groupIds.add((String) target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class).get("id"));
        // Create user jsmith
        body = new HashMap<>();
        String firstName = "Jonh";
        String lastName = "Smith";
        String login = "jsmith";
        body.put("firstName", firstName);
        body.put("lastName", lastName);
        body.put("login", login);
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String userId = (String) answer.get("id");
        // Add group clerks to jsmith
        body = new HashMap<>();
        body.put("memberships", Collections.singleton(clerks));
        assert target
                .path("users")
                .path(userId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2;
        // Assert group added
        answer = target
                .path("users")
                .path(userId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        List<String> memberships;
        memberships = (List<String>) answer.get("memberships");
        assert memberships.contains(clerks);
        // Add group managers to jsmith
        body = new HashMap<>();
        body.put("memberships", Collections.singleton(managers));
        assert target
                .path("users")
                .path(userId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(entity(body))
                .getStatus() / 100 == 2;
        // Assert group added, and old group gone
        answer = target
                .path("users")
                .path(userId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        memberships = (List<String>) answer.get("memberships");
        assert memberships.contains(managers);
        assert !memberships.contains(clerks);
        // Delete user
        assert target
                .path("users")
                .path(userId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
        // Delete groups
        for (String groupId : groupIds) {
            assert target
                    .path("groups")
                    .path(groupId)
                    .request(MEDIA_TYPE)
                    .header(AUTH_HEADER, this.getAuthHeaderValue())
                    .delete()
                    .getStatus() / 100 == 2;
        }
    }

    @Test
    public void testListUser() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String login = "jsmith";
        body.put("login", login);
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        answer = target
                .path("users")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        BigDecimal one = new BigDecimal(1);
        assert one.equals(answer.get("total")) : "User list total mismatch";
        assert one.equals(answer.get("size")) : "User list size mismatch";
        Map<?, ?> user = (Map<?, ?>) ((List<?>) answer.get("objects")).get(0);
        assert login.equals(user.get("login")) : "User list result mismatch";
        assert target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testListGroup() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "clerks";
        body.put("name", name);
        answer = target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        answer = target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        BigDecimal one = new BigDecimal(1);
        assert one.equals(answer.get("total")) : "Group list total mismatch";
        assert one.equals(answer.get("size")) : "Group list size mismatch";
        Map<?, ?> user = (Map<?, ?>) ((List<?>) answer.get("objects")).get(0);
        assert name.equals(user.get("name")) : "Group list result mismatch";
        assert target
                .path("groups")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testListGroupByNameHit() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "clerks";
        body.put("name", name);
        answer = target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        answer = target
                .path("groups")
                .queryParam("name", "cl*")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        BigDecimal one = new BigDecimal(1);
        assert one.equals(answer.get("total")) : "Group list total mismatch";
        assert one.equals(answer.get("size")) : "Group list size mismatch";
        Map<?, ?> user = (Map<?, ?>) ((List<?>) answer.get("objects")).get(0);
        assert name.equals(user.get("name")) : "Group list result mismatch";
        assert target
                .path("groups")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    public void testListGroupByNameMiss() {
        Map<?, ?> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "clerks";
        body.put("name", name);
        answer = target
                .path("groups")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(entity(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        answer = target
                .path("groups")
                .queryParam("name", "kl*")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        BigDecimal zero = new BigDecimal(0);
        assert zero.equals(answer.get("total")) : "Group list total mismatch";
        assert zero.equals(answer.get("size")) : "Group list size mismatch";
        assert target
                .path("groups")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }
}
