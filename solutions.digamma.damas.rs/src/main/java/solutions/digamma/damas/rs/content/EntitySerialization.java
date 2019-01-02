package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Modifiable;

import java.time.ZonedDateTime;

public abstract class EntitySerialization implements Entity, Created, Modifiable {

    protected String id;
    protected String createdBy;
    protected ZonedDateTime creationDate;
    protected String modifiedBy;
    protected ZonedDateTime modificationDate;

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
    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String getModifiedBy() {
        return this.modifiedBy;
    }

    @Override
    public ZonedDateTime getModificationDate() {
        return this.modificationDate;
    }
}
