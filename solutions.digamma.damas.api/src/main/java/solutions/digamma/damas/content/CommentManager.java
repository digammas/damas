package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;

/**
 * Comment manager.
 *
 * @author Ahmad Shahwan
 */
public interface CommentManager
        extends CrudManager<Comment>, SearchEngine<Comment> {
}
