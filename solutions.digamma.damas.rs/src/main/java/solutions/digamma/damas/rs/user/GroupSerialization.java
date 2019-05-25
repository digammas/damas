package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.user.Group;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Group")
public class GroupSerialization extends SubjectSerialization implements Group {

    private String name;
    private String label;

    public GroupSerialization() {
        super();
    }

    protected GroupSerialization(Group clone) {
        super(clone);
        this.name = clone.getName();
        this.label = clone.getLabel();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void setLabel(String value) {
        this.label = value;
    }

    public static GroupSerialization from(Group clone) {
        return new GroupSerialization(clone);
    }
}
