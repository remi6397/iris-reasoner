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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.alg.CycleDetector;
import org._3pq.jgrapht.graph.DirectedMultigraph;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.graph.IPredicateGraph;

/**
 * @author richi
 * 
 */
public class PredicateGraph implements IPredicateGraph {

	private DirectedGraph g = new DirectedMultigraph();

	private CycleDetector cd = new CycleDetector(g);

	PredicateGraph() {
	}

	public void addRule(final IRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		for (ILiteral h : rule.getHeadLiterals()) {
			g.addVertex(h.getPredicate());
			for (ILiteral b : rule.getBodyLiterals()) {
				boolean doubled = false;
				g.addVertex(b.getPredicate());
				Edge e = new LabeledDirectedEdge(b.getPredicate(), h
						.getPredicate(), b.isPositive());
				List edges = g.getAllEdges(b.getPredicate(), h.getPredicate());

				for (Object edge : edges) {
					if (e.equals(edge)) {
						doubled = true;
						break;
					}
				}
				if (!doubled) {
					g.addEdge(e);
				}
			}
		}
	}

	public boolean detectCycles() {
		return cd.detectCycles();
	}

	public Set findVertexesForCycle() {
		return cd.findCycles();
	}

	/**
	 * This method returns a &quot;set&quot; of edges which are involved in the
	 * cycle.<br/><br/>NOTE: This Set doesn't behave like a &quot;normal
	 * set&quot; in the meaning, that it is possible to store multible equal
	 * object in this set. This is a problem of the underlying implementation,
	 * which is using <code>HashSet</code> and doesn't implement the
	 * <code>hashCode</code> method for the objects stored in this set. So, to
	 * check whether a edge is in the set you sould iterate through the set and
	 * compare the objects using the equals method. This should work, because
	 * the edges (LabeledDirectedEdge) used in this graph are impelmenting this
	 * mehtod.
	 * 
	 * @see LabeledDirectedEdge
	 * @return the set of edges
	 */
	public Set<Edge> findEdgesForCycle() {
		Set cycle = findVertexesForCycle();
		Set<Edge> edges = new HashSet<Edge>();
		for (Object v : cycle) {
			List s = GraphHelper.successorListOf(g, v);
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
		int neg = 0;
		for (Object e : findEdgesForCycle()) {
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
}
