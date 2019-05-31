package solutions.digamma.damas.rs.common;

import java.time.ZonedDateTime;
import solutions.digamma.damas.search.Filter;

public class SearchFilter implements Filter {

    private String scopeId;
    private Boolean recursive;
    private String namePattern;
    private ZonedDateTime createdBefore;
    private ZonedDateTime createdAfter;
    private ZonedDateTime lastModifiedBefore;
    private ZonedDateTime lastModifiedAfter;
    private String createdBy;
    private String lastModifiedBy;

    public SearchFilter() {
        super();
    }

    @Override
    public String getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public Boolean isRecursive() {
        return Boolean.TRUE.equals(recursive);
    }

    @Override
    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    @Override
    public String getNamePattern() {
        return namePattern;
    }

    @Override
    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    @Override
    public ZonedDateTime getCreatedBefore() {
        return createdBefore;
    }

    @Override
    public void setCreatedBefore(ZonedDateTime createdBefore) {
        this.createdBefore = createdBefore;
    }

    @Override
    public ZonedDateTime getCreatedAfter() {
        return createdAfter;
    }

    @Override
    public void setCreatedAfter(ZonedDateTime createdAfter) {
        this.createdAfter = createdAfter;
    }

    @Override
    public ZonedDateTime getLastModifiedBefore() {
        return lastModifiedBefore;
    }

    @Override
    public void setLastModifiedBefore(ZonedDateTime lastModifiedBefore) {
        this.lastModifiedBefore = lastModifiedBefore;
    }

    @Override
    public ZonedDateTime getLastModifiedAfter() {
        return lastModifiedAfter;
    }

    @Override
    public void setLastModifiedAfter(ZonedDateTime lastModifiedAfter) {
        this.lastModifiedAfter = lastModifiedAfter;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
