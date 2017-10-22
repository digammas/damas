package solutions.digamma.damas.jcr.model

import solutions.digamma.damas.entity.CrudManager
import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.content.PathFinder
import solutions.digamma.damas.entity.SearchEngine

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
abstract class JcrFullManager<T : Entity> : JcrCrudManager<T>(), CrudManager<T>, SearchEngine<T>, PathFinder<T>
