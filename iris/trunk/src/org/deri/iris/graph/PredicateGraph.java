/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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

// TODO: implement equals, hashCode, toString an clone.

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.alg.ConnectivityInspector;
import org._3pq.jgrapht.alg.CycleDetector;
import org._3pq.jgrapht.graph.DirectedMultigraph;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.graph.IPredicateGraph;

/**
 * <p>
 * A graph to determine the dependencies of rules and predicates to each other.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 */
public class PredicateGraph implements IPredicateGraph {

	/** Comparator to order rules according to their dependencies. */
	private final RuleComparator rc = new RuleComparator();

	/** Comparator to order predicates according to their dependencies. */
	private final PredicateComparator pc = new PredicateComparator();

	/** Graph to represent the dependencies of the predicates. */
	private final DirectedGraph g = new DirectedMultigraph();

	/** Cycle detector, to determine, whether the rules are recursive. */
	private final CycleDetector cd = new CycleDetector(g);

	/**
	 * Connectivity inspector to determine, whether paths between vertices
	 * exists.
	 */
	private final ConnectivityInspector ci = new ConnectivityInspector(g);

	/**
	 * Constructs an empty graph object.
	 */
	PredicateGraph() {
	}

	public void addRule(final IRule rule) {
		if (rule == null) {
			throw new NullPointerException("The rule must not be null");
		}

		for (final ILiteral h : rule.getHeadLiterals()) {
			final IPredicate hp = h.getPredicate();
			g.addVertex(hp);

			for (final ILiteral l : rule.getBodyLiterals()) {
				boolean doubled = false;
				final IPredicate p = l.getPredicate();
				final Edge e = new LabeledDirectedEdge<Boolean>(p, hp, l
						.isPositive());

				g.addVertex(p);

				// determining, whether an equal edge is already in the graph
				// TODO: use the contains(Object) method
				for (final Object edge : g.getAllEdges(p, hp)) {
					if (e.equals(edge)) {
						doubled = true;
						break;
					}
				}
				if (!doubled) { // if there is no such edge, add the new one
					g.addEdge(e);
				}
			}
		}
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
		return cd.detectCycles();
	}

	public Set findVertexesForCycle() {
		return cd.findCycles();
	}

	public Set<Edge> findEdgesForCycle() {
		final Set cycle = findVertexesForCycle();
		final Set<Edge> edges = new HashSet<Edge>();
		for (final Object v : cycle) {
			final List s = GraphHelper.successorListOf(g, v);
			for (Object p : s) {
				if (cycle.contains(p)) {
					edges.add(g.getEdge(v, p));
					break;
				}
			}
		}
		assert (edges.size() != cycle.size()) : "the number of edges and vertexes must be equal";
		return edges;
	}

	public int countNegativesForCycle() {
		// TODO: optimize this
		int neg = 0;
		for (final Object e : findEdgesForCycle()) {
			if (e instanceof LabeledDirectedEdge) {
				Object l = ((LabeledDirectedEdge) e).getLabel();
				if (l instanceof Boolean) {
					if (!((Boolean) l)) {
						neg++;
					}
				}
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
		if (!g.containsVertex(p)) {
			return Collections.EMPTY_SET;
		}

		final Set<IPredicate> todo = new HashSet<IPredicate>();
		todo.add(p);
		final Set<IPredicate> deps = new HashSet<IPredicate>();

		while (!todo.isEmpty()) {
			final IPredicate act = todo.iterator().next();
			todo.remove(act);

			for (final IPredicate depends : (List<IPredicate>) GraphHelper
					.predecessorListOf(g, act)) {
				if (!deps.contains(depends)) {
					todo.add(depends);
				}
				deps.add(depends);
			}
		}

		return deps;
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
	 * @date $Date$
	 * @see PredicateComparator
	 */
	private class RuleComparator implements Comparator<IRule> {

		public int compare(final IRule o1, final IRule o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("None of the rule must be null");
			}
			if ((o1.getHeadLenght() != 1) || (o2.getHeadLenght() != 1)) {
				throw new IllegalArgumentException(
						"Only rules with a headlength of 1 are allowed.");
			}
			return pc.compare(o1.getHeadLiteral(0).getPredicate(), o2
					.getHeadLiteral(0).getPredicate());
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
	 * @date $Date$
	 */
	private class PredicateComparator implements Comparator<IPredicate> {

		public int compare(final IPredicate o1, final IPredicate o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException(
						"None of the predicates must be null");
			}

			// one of the vertices is not in the graph, or there is no
			// connection of the vertices -> return 0
			if (!g.containsVertex(o1) || !g.containsVertex(o2)
					|| !ci.pathExists(o1, o2)) {
				return 0;
			}
			// determine who depends on who
			return getDepends(o1).contains(o2) ? 1 : -1;
		}
	}
}
