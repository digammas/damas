package solutions.digamma.damas.jcr.model;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.PathFinder;
import solutions.digamma.damas.SearchEngine;

/**
 * JCR full manager.
 *
 * @author Ahmad Shahwan
 */
abstract public class JcrFullManager<T extends Entity>
    extends JcrCrudManager<T>
    implements CrudManager<T>, SearchEngine<T>, PathFinder<T> {
}
