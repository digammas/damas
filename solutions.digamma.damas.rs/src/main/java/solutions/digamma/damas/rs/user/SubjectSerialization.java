package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.user.Subject;

public abstract class SubjectSerialization implements Subject {

    public SubjectSerialization() {
    }

    protected SubjectSerialization(Subject clone) throws WorkspaceException {
        this.id = clone.getId();
        this.enabled = clone.isEnabled();
    }

    protected String id;
    protected Boolean enabled;

    @Override
    public Boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(Boolean value) {
        this.enabled = value;
    }

    @Override
    public String getId() throws WorkspaceException {
        return this.id;
    }
}
