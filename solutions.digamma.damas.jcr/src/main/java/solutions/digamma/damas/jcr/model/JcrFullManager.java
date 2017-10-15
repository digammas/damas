package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.content.PathFinder;
import solutions.digamma.damas.entity.SearchEngine;

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrFullManager<T extends Entity>
    extends JcrCrudManager<T>
    implements CrudManager<T>, SearchEngine<T>, PathFinder<T> {
}
