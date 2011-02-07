/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.graph;

// TODO: implement equals, hashCode an clone.

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.graph.ILabeledEdge;
import org.deri.iris.api.graph.IPredicateGraph;

import eu.soa4all.graph.Direction;
import eu.soa4all.graph.Edge;
import eu.soa4all.graph.Graph;
import eu.soa4all.graph.Graphs;
import eu.soa4all.graph.impl.GraphFactory;

/**
 * <p>
 * A graph to determine the dependencies of rules and predicates to each other.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class PredicateGraph implements IPredicateGraph {

	/** Comparator to order rules according to their dependencies. */
	private final RuleComparator rc = new RuleComparator();

	/** Comparator to order predicates according to their dependencies. */
	private final PredicateComparator pc = new PredicateComparator();

	/** Graph to represent the dependencies of the predicates. */
	private final Graph<IPredicate, Boolean> g;

	/**
	 * Constructs an empty graph object.
	 */
	PredicateGraph() {
		g = Helper.createGraph();
	}

	/**
	 * Constructs a new graph with a given set of rules.
	 * @param r the rules with which to initialize the graph
	 */
	PredicateGraph(final Collection<IRule> r) {
		g = Helper.createGraph(r);
	}

	public void addRule(final IRule rule) {
		Helper.addRule(g, rule);
	}


	public void addRule(final Collection<IRule> r) {
		if ((r == null) || r.contains(null)) {
			throw new NullPointerException(
					"The rules must not be, or contain null");
		}
		for (final IRule rule : r) {
			addRule(rule);
		}
	}

	public boolean detectCycles() {
		return g.isCyclic();
	}

	public Set<IPredicate> findVertexesForCycle() {
		Set<Graph<IPredicate, Boolean>> cycles = Graphs.findCycles(g);
		if (cycles.size() == 0)
			throw new AssertionError("There must be at least one cycle");
		
		Graph<IPredicate, Boolean> cycle = cycles.iterator().next();
		
		return cycle.getVertices();
	}

	public Set<Edge<IPredicate, Boolean>> findEdgesForCycle() {
		final Set<IPredicate> cycle = findVertexesForCycle();
		final Set<Edge<IPredicate, Boolean>> edges = 
			new HashSet<Edge<IPredicate, Boolean>>();
		for (final IPredicate v : cycle) {
			for (final IPredicate p : Graphs.sort(g, v)) {
				if (cycle.contains(p)) {
					Set<Edge<IPredicate, Boolean>> vpEdges = g.getEdges(v, p); // FIXME might be more than one!
					edges.add(vpEdges.iterator().next());
					break;
				}
			}
		}
		assert (edges.size() == cycle.size()) : "the number of edges and vertexes must be equal";
		return edges;
	}

	public int countNegativesForCycle() {
		int neg = 0;
		for (final Edge<IPredicate, Boolean> e : findEdgesForCycle()) {
			if (!e.getWeight()) {
				neg++;
			}
		}
		return neg;
	}

	public Comparator<IRule> getRuleComparator() {
		return rc;
	}

	public Comparator<IPredicate> getPredicateComparator() {
		return pc;
	}

	public Set<IPredicate> getDepends(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (!g.getVertices().contains(p)) {
			return Collections.emptySet();
		}

		final Set<IPredicate> todo = new HashSet<IPredicate>();
		todo.add(p);
		final Set<IPredicate> deps = new HashSet<IPredicate>();

		while (!todo.isEmpty()) {
			final IPredicate act = todo.iterator().next();
			todo.remove(act);

			for (final IPredicate depends : Graphs.sort(g, act, Direction.BACKWARD)) {
				if (deps.add(depends)) {
					todo.add(depends);
				}
			}
		}

		return deps;
	}

	/**
	 * <p>
	 * Computes a short description of this object. <b>The format of the
	 * returned string is undocumented and subject to change.</b>.
	 * </p>
	 * <p>
	 * And example return string could be:
	 * </p>
	 * <p>
	 * <pre><code>
	 * a-&gt;(false)-&gt;b
	 * b-&gt;(true)-&gt;c
	 * </code></pre>
	 * </p>
	 * @return the string description
	 */
	public String toString() {
		final StringBuilder b = new StringBuilder();
		for (final Edge<IPredicate, Boolean> e : g.getEdges()) {
			b.append(e).append(System.getProperty("line.separator"));
		}
		return b.toString();
	}
			

	/**
	 * <p>
	 * Compares two rules depending on their dependencies of each other.
	 * </p>
	 * <p>
	 * The rules will compared according to their headpredicates.
	 * </p>
	 * <p>
	 * $Id$
	 * </p>
	 * 
	 * @author richi
	 * @version $Revision$
	 * @see PredicateComparator
	 */
	private class RuleComparator implements Comparator<IRule> {

		public int compare(final IRule o1, final IRule o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("None of the rule must be null");
			}
			if ((o1.getHead().size() != 1) || (o2.getHead().size() != 1)) {
				throw new IllegalArgumentException(
						"Only rules with a headlength of 1 are allowed.");
			}
			return pc.compare(o1.getHead().get(0).getAtom().getPredicate(), 
					o2.getHead().get(0).getAtom().getPredicate());
		}
	}

	/**
	 * <p>
	 * Compares two predicates depending on their dependencies of their rules.
	 * </p>
	 * <p>
	 * If one of the compared predicate isn't in the graph, or there isn't a
	 * path from one predicate to the other {@code 0} will be returned. If the
	 * first predicate depends on the second one, the first one will be
	 * determined to be bigger, and vice versa.
	 * </p>
	 * <p>
	 * $Id$
	 * </p>
	 * 
	 * @author richi
	 * @version $Revision$
	 */
	private class PredicateComparator implements Comparator<IPredicate> {

		public int compare(final IPredicate o1, final IPredicate o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException(
						"None of the predicates must be null");
			}

			// one of the vertices is not in the graph, or there is no
			// connection of the vertices -> return 0
			if (!g.getVertices().contains(o1) || !g.getVertices().contains(o2)
					|| !(Graphs.findPath(g, o1, o2) != null)) { // FIXME check return value null
				return 0;
			}
			// determine who depends on who
			return getDepends(o1).contains(o2) ? 1 : -1;
		}
	}

	private static class Helper {
		private static GraphFactory GRAPH = GraphFactory.getInstance();
		
		
		static Graph<IPredicate, Boolean> createGraph() {
			return createGraph(Collections.<IRule>emptyList());
		}
			
		static Graph<IPredicate, Boolean> createGraph(final Collection<IRule> r) {
			Graph<IPredicate, Boolean> graph = GRAPH.createGraph(true);
			
			if (r != null) {
				for (final IRule rule : r) {
					addRule(graph, rule);
				}
			}
			
			return graph;
		}
		
		static Edge<IPredicate, Boolean> createEdge(final IPredicate s, final IPredicate t, boolean label) {
			if ((s == null) || (t == null)) {
				throw new NullPointerException("The vertices must not be null");
			}
			return GRAPH.createEdge(s, t, label);
		}
	
		static void addRule(final Graph<IPredicate, Boolean> g, final IRule rule) {
			if (rule == null) {
				throw new NullPointerException("The rule must not be null");
			}

			for (final ILiteral h : rule.getHead()) {
				final IPredicate hp = h.getAtom().getPredicate();
				g.add(hp);

				for (final ILiteral l : rule.getBody()) {
					final IPredicate p = l.getAtom().getPredicate();
					final Edge<IPredicate, Boolean> e = 
						createEdge(p, hp, l.isPositive());

					g.add(p);

					// if there is no such edge, add the new one
					if (!g.getEdges().contains(e)) { // FIXME equality by reference
						g.add(e);
					}
				}
			}
		}

	}
}
