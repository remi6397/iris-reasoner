package at.sti2.streamingiris.api.factory;

import java.util.Collection;

import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.graph.IPredicateGraph;

/**
 * <p>
 * An interface that can be used to create a predicate graph.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public interface IGraphFactory {

	/**
	 * Constructs a new empty predicate graph.
	 * 
	 * @return the constructed graph
	 */
	public IPredicateGraph createPredicateGraph();

	/**
	 * Constructs a new predicate graph initialized with a collection of rules.
	 * 
	 * @param r
	 *            the rules with which to initialize the graph
	 * @return the constructed graph
	 */
	public IPredicateGraph createPredicateGraph(final Collection<IRule> r);
}
