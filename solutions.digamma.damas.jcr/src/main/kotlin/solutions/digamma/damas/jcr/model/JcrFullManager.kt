package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.entity.CrudManager
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.content.PathFinder
import solutions.digamma.damas.search.Filter
import solutions.digamma.damas.search.SearchEngine

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
internal abstract class JcrFullManager<T : Entity> :
        JcrCrudManager<T>(),
        CrudManager<T>,
        SearchEngine<T, Filter>,
        PathFinder<T>
