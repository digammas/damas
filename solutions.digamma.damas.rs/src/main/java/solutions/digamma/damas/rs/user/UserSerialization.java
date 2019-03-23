package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "User")
public class UserSerialization extends SubjectSerialization implements User {


    private String emailAddress;
    private String firstName;
    private String lastName;
    private List<String> memberships = new ArrayList<>();

    public UserSerialization() {
    }

    protected UserSerialization(User clone) throws WorkspaceException {
        super(clone);
        this.emailAddress = clone.getEmailAddress();
        this.firstName = clone.getFirstName();
        this.lastName = clone.getLastName();
        this.memberships = clone.getMemberships();
    }

    @Override
    public String getLogin() {
        return this.id;
    }

    @Override
    public String getEmailAddress() {
        return this.emailAddress;
    }

    @Override
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(String value) {
        this.firstName = value;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setLastName(String value) {
        this.lastName = value;
    }

    @Override
    public List<String> getMemberships() {
        return this.memberships;
    }

    public static UserSerialization from(User clone) throws WorkspaceException {
        return new UserSerialization(clone);
    }
}
