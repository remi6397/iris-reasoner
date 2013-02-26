package at.sti2.streamingiris.graph;

import java.util.Collection;

import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.factory.IGraphFactory;
import at.sti2.streamingiris.api.graph.IPredicateGraph;

/**
 * <p>
 * A simple predicate graph implementation.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision$
 */
public class GraphFactory implements IGraphFactory {

	private static final IGraphFactory FACTORY = new GraphFactory();

	private GraphFactory() {
		// this is a singelton
	}

	public static IGraphFactory getInstance() {
		return FACTORY;
	}

	public IPredicateGraph createPredicateGraph() {
		return new PredicateGraph();
	}

	public IPredicateGraph createPredicateGraph(final Collection<IRule> r) {
		return new PredicateGraph(r);
	}

}
