package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.CommentManager;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;
import solutions.digamma.damas.rs.common.SearchEnabledCrudResource;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("comments")
public class CommentResource
        extends SearchEnabledCrudResource<Comment, CommentSerialization> {

    @Inject
    protected CommentManager manager;

    @Override
    protected SearchEngine<Comment> getSearchEngine() {
        return this.manager;
    }

    @Override
    protected CrudManager<Comment> getManager() {
        return this.manager;
    }

    @Override
    protected CommentSerialization wrap(Comment entity) {
        return CommentSerialization.from(entity);
    }
}
