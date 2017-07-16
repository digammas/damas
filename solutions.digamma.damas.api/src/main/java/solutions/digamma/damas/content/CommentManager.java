package solutions.digamma.damas.content;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.SearchEngine;

/**
 * Comment manager.
 *
 * @author Ahmad Shahwan
 */
public interface CommentManager
        extends CrudManager<Comment>, SearchEngine<Comment> {
}
