package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.user.Group;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Group")
public class GroupSerialization extends SubjectSerialization implements Group {

    private String name;

    public GroupSerialization() {
        super();
    }

    protected GroupSerialization(Group clone) throws WorkspaceException {
        super(clone);
        this.name = clone.getName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String value) {
        this.name = value;
    }

    public static GroupSerialization from(Group clone) throws WorkspaceException {
        return new GroupSerialization(clone);
    }
}
