package solutions.digamma.damas.integration;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

@RunWith(ContainerRunner.class)
@Singleton
public class AuthTest extends IntegrationTest {

    @Test
    public void testGetAuth() {
        Map<?, ?> answer = target
                .path("auth")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert "admin".equals(answer.get("userLogin")) :
                String.format("Wrong userLogin %s", answer.get("userLogin"));
    }

    @Test
    public void testGetAuthNewUser() {
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
                .readEntity(Map.class);
        assert login.equals(auth.get("userLogin")) : String.format(
                "Wrong userLogin in UserToken %s",
                answer.get("userLogin")
        );
        String token = (String) auth.get("secret");
        answer = target
                .path("auth")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue(token))
                .get()
                .readEntity(Map.class);
        assert login.equals(answer.get("userLogin")) : String.format(
                "Wrong userLogin in UserSession %s",
                answer.get("userLogin")
        );

        /* Logout user */
        target
                .path("auth")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, token)
                .delete();

        target
                .path("users")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete();
    }
}
