package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Modifiable;

import java.util.Calendar;

public abstract class EntitySerialization implements Entity, Created, Modifiable {

    protected String id;
    protected String createdBy;
    protected Calendar creationDate;
    protected String modifiedBy;
    protected Calendar modificationDate;

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Set ID.
     *
     * @param value
     */
    public void setId(String value) {
        this.id = value;
    }

    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    @Override
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String getModifiedBy() {
        return this.modifiedBy;
    }

    @Override
    public Calendar getModificationDate() {
        return this.modificationDate;
    }
}
