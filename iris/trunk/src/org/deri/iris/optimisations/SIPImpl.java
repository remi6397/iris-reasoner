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
package org.deri.iris.optimisations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

import org.deri.iris.factory.Factory;
import org.deri.iris.graph.LabeledEdge;
import org.deri.iris.optimisations.AdornedProgram.AdornedPredicate;
import org.deri.iris.optimisations.Adornment;
import org.deri.iris.optimisations.ISip;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;

// FIXME: most Set<IVariable> should be Set<ITerm> and contain !isGround() terms
// FIXME: what if the vertices aren't connected?

/**
 * <p>
 * A SIP Implementation according to the &quot;The Power of Magic&quot; paper.
 * </p>
 * <p>
 * This class is final, because the conscructor is calling public nonfinal
 * methods.
 * </p>
 * <p>
 * $Id: SIPImpl.java,v 1.28 2007-10-30 10:35:49 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.28 $
 */
public final class SIPImpl implements ISip {
	/**
	 * Comparator to compare literals according to their position in the sips.
	 */
	private final Comparator<ILiteral> LITERAL_COMP = new LiteralComparator();

	/** The graph on which the variables are padded along. */
	private DirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>> sipGraph = 
		new DefaultDirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>>(new SipEdgeFactory());

	/**
	 * Creates a SIP for the given rule with bindings for the given query.<b>
	 * NOTE: at the moment only the first literal of the head and the query are
	 * recognized.</b>
	 * 
	 * @param r
	 *            the rule for which to construct the graph
	 * @param q
	 *            the query for this rule
	 * @throws NullPointerException
	 *             if the rule or the query is null
	 * @throws IllegalArgumentException
	 *             if the symbol and the arity of the head of the rule and the
	 *             query doesn't match
	 */
	public SIPImpl(final IRule r, final IQuery q) {
		if ((r == null) || (q == null)) {
			throw new NullPointerException();
		}
		if (r.getHead().size() != 1) {
			throw new IllegalArgumentException(
					"At the moment only rule heads with length 1 are allowed");
		}
		if (q.getLiterals().size() != 1) {
			throw new IllegalArgumentException(
					"At the moment only queries with length 1 are allowed");
		}

		final ILiteral headLiteral = r.getHead().get(0);
		final ILiteral queryLiteral = q.getLiterals().get(0);
		final IPredicate headPredicate = headLiteral.getAtom().getPredicate();
		final IPredicate queryPredicate = queryLiteral.getAtom().getPredicate();

		// this check is only neccessary for the first implementation
		// allow only one head
		// head predicate must be equals with the query predicate

		// I'm not using equals, because the query could be adorned, and then
		// they won't be equal
		if (!headPredicate.getPredicateSymbol().equals(
				queryPredicate.getPredicateSymbol())
				|| (headPredicate.getArity() != queryPredicate.getArity())) {
			throw new IllegalArgumentException(
					"The symbol and arity of the predicates of "
							+ "the rule head and the query must match");
		}

		// determining the known variables of the head
		final Set<IVariable> assumedKnown = new HashSet<IVariable>();
		for (final Iterator<ITerm> headTerms = headLiteral.getAtom().getTuple()
				.iterator(), queryTerms = queryLiteral.getAtom().getTuple()
				.iterator(); (headTerms.hasNext() && queryTerms
				.hasNext());) {
			final ITerm headT = headTerms.next();
			final ITerm queryT = queryTerms.next();
			if (queryT.isGround() && !headT.isGround()) {
				assumedKnown.addAll(getVariables(headT));
			}
		}
		
		// ensure that a save rule will result in a save sip and
		// construct the sip
		constructSip(orderLiterals(r, assumedKnown), assumedKnown);
	}

	/**
	 * Constructs a sip out of a rule.
	 * @param r the rule for which to create the sip
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 */
	public SIPImpl(final IRule r) {
		if (r == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		final Set<IVariable> known = new HashSet<IVariable>();
		for (final ILiteral l : r.getHead()) {
			if (l.getAtom().getPredicate() instanceof AdornedPredicate) {
				final Adornment[] ad = ((AdornedPredicate) l.getAtom().getPredicate()).getAdornment();
				int i = 0;
				for (final ITerm t : l.getAtom().getTuple()) {
					if (ad[i++] == Adornment.BOUND) {
						known.addAll(getVariables(t));
					}
				}
			}
		}

		constructSip(r, known);
	}

	/**
	 * Returns the variables of a term. If the term is <code>null</code> or
	 * ground a empty set will be returned, if it is a constructed term all
	 * of its variables are returned, otherwise (in this case it must be a 
	 * variable) the term itself in a set will be returned.
	 * @param t the term for which to return teh varaibles
	 * @return the set of variables in the term
	 */
	private static Set<IVariable> getVariables(final ITerm t) {
		if ((t == null) || t.isGround()) {
			return Collections.EMPTY_SET;
		} else if (t instanceof IConstructedTerm) {
			return ((IConstructedTerm) t).getVariables();
		}
		return Collections.singleton((IVariable) t);
	}

	/**
	 * Constructs the sip for a given rule and a known set of variables.
	 * @param r the rule
	 * @param assumedKnown the known variables
	 */
	private void constructSip(final IRule r, final Set<IVariable> assumedKnown) {
		assert r != null: "The rule must not be null";
		assert assumedKnown != null: "The known collection must not be null";
		assert r.getHead().size() == 1: "At the moment we only can " + 
			"construct a sip for a rule with length of 1";

		// add all literals to the sip
		for (final ILiteral l : r.getHead()) {
			sipGraph.addVertex(l);
		}
		for (final ILiteral l : r.getBody()) {
			sipGraph.addVertex(l);
		}

		// map containing the variable->passedFromLiterals mappings
		final Map<IVariable, Set<ILiteral>> passings = new HashMap<IVariable, Set<ILiteral>>();
		// add the passings from head
		final ILiteral head = r.getHead().get(0);
		for (final IVariable v : assumedKnown) {
			final Set<ILiteral> passedFrom = new HashSet<ILiteral>();
			passedFrom.add(head);
			passings.put(v, passedFrom);
		}

		// iterating over the body literals and check the passings for
		// the variables of the literals
		for (final ILiteral l : r.getBody()) {
			for (final IVariable v : l.getAtom().getTuple().getVariables()) {
				Set<ILiteral> passedFrom = passings.get(v);
				if ((passedFrom != null) && !passedFrom.isEmpty()) { // there are some passings
					for (final ILiteral passingLiteral : passedFrom) {
						addEdge(passingLiteral, l, Collections.singleton(v));
					}
				}
				// add this literal to the list of passing
				// literals for this variable
				if (passedFrom == null) {
					passedFrom = new HashSet<ILiteral>();
					passings.put(v, passedFrom);
				}
				passedFrom.add(l);
			}
		}
	}

	/**
	 * Updates the sip. Adds a edge with the given source, target and given
	 * label to the sip. If a edge with the given source and target already
	 * exists, the label will be updated.
	 * 
	 * @throws NullPointerException
	 *             if the source, target or passedTo is {@code null}
	 */
	private void addEdge(final ILiteral source, final ILiteral target,
			final Set<IVariable> passedTo) {
		assert source != null: "The source must not be null";
		assert target != null: "The target must not be null";
		assert passedTo != null: "The passed variables must not be null";

		final LabeledEdge<ILiteral, Set<IVariable>> edge = sipGraph.getEdge(source, target);
		if (edge != null) { // updating the edge
			edge.getLabel().addAll(passedTo);
		} else { // adding a new edge
			// we assume, that all literals are already added to the
			// sip
			sipGraph.addEdge(source, 
					target, 
					new LabeledEdge<ILiteral, Set<IVariable>>(source, 
						target, 
						new HashSet<IVariable>(passedTo)));
		}
	}

	public Set<IVariable> getBoundVariables(final ILiteral l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		final Set<IVariable> variables = new HashSet<IVariable>();

		for (final ILiteral predec : Graphs.predecessorListOf(sipGraph, l)) {
			variables.addAll(sipGraph.getEdge(predec, l).getLabel());
		}
		return variables;
	}

	public Set<ILiteral> getDepends(final ILiteral l) {
		final Set<ILiteral> dependencies = new HashSet<ILiteral>();
		final Set<ILiteral> todoDependencies = new HashSet<ILiteral>();
		if (l == null) {
			throw new NullPointerException();
		}

		todoDependencies.add(l);
		while (!todoDependencies.isEmpty()) {
			final ILiteral actual = todoDependencies.iterator().next();
			todoDependencies.remove(actual);

			final List<ILiteral> vertices = Graphs.predecessorListOf(sipGraph,
					actual);
			dependencies.addAll(vertices);
			todoDependencies.addAll(vertices);
		}

		return dependencies;
	}

	public Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdgesEnteringLiteral(
			final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		final List<ILiteral> predecessors = Graphs.predecessorListOf(sipGraph, l);
		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = 
			new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>(predecessors.size());
		for (final ILiteral o : predecessors) {
			edges.add(sipGraph.getEdge(o, l));
		}
		return edges;
	}

	public Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdgesLeavingLiteral(
			final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		final List<ILiteral> successors = Graphs.successorListOf(sipGraph, l);
		final Set<LabeledEdge<ILiteral, Set<IVariable>>> edges = 
			new HashSet<LabeledEdge<ILiteral, Set<IVariable>>>(successors.size());
		for (final ILiteral o : successors) {
			edges.add(sipGraph.getEdge(l, o));
		}
		return edges;
	}

	public Set<IVariable> variablesPassedByLiteral(final ILiteral source,
			final ILiteral target) {
		if ((source == null) || (target == null)) {
			throw new NullPointerException(
					"The source and the target must not be null");
		}
		Set<IVariable> vars = new HashSet<IVariable>(getBoundVariables(source));
		vars.addAll((sipGraph.getEdge(source, target)).getLabel());
		return vars;
	}

	/**
	 * Returns a simple string representation of this graph. <b>The subject of
	 * the returned string is to change.</b> The returned string may be a list
	 * of all edges of this graph separated by newlines.
	 * 
	 * @return the string representation
	 */
	public String toString() {
		final String NEWLINE = System.getProperty("line.separator");
		StringBuilder buffer = new StringBuilder();
		for (LabeledEdge<ILiteral, Set<IVariable>> o : sipGraph.edgeSet()) {
			buffer.append(o).append(NEWLINE);
		}
		return buffer.toString();
	}

	public boolean containsVertex(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		return sipGraph.containsVertex(l);
	}

	public Set<ILiteral> getRootVertices() {
		final Set<ILiteral> roots = new HashSet<ILiteral>();
		for (final ILiteral o : sipGraph.vertexSet()) {
			if (Graphs.predecessorListOf(sipGraph, o).isEmpty()) {
				roots.add(o);
			}
		}
		return roots;
	}

	public Set<ILiteral> getLeafVertices() {
		final Set<ILiteral> leafes = new HashSet<ILiteral>();
		for (final ILiteral o : sipGraph.vertexSet()) {
			if (Graphs.successorListOf(sipGraph, o).isEmpty()) {
				leafes.add(o);
			}
		}
		return leafes;
	}

	public Comparator<ILiteral> getLiteralComparator() {
		return LITERAL_COMP;
	}

	/**
	 * Returns a unmodifiable set of all edges of the sip.
	 * 
	 * @return the set of edges.
	 */
	public Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdges() {
		return Collections.unmodifiableSet(sipGraph.edgeSet());
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SIPImpl)) {
			return false;
		}
		final SIPImpl s = (SIPImpl) o;
		return sipGraph.edgeSet().equals(s.sipGraph.edgeSet());
	}

	public int hashCode() {
		int res = 17;
		res = res * 37 + sipGraph.edgeSet().hashCode();
		return res;
	}

	/**
	 * Orders the literals of the rule so that a save rule won't produce an
	 * unsave sip. <b>At the moment only negative literals are handeled, but not
	 * builtis.</b>
	 * 
	 * @param r
	 *            the rule for which to sort the literals
	 * @param known
	 *            the variables which are known in the head
	 * @throws NullPointerException
	 *             if the rule is {@code null}
	 * @throws NullPointerException
	 *             if the known collection is or contains {@code null}
	 */
	static IRule orderLiterals(final IRule r,
			final Collection<IVariable> known) {
		// TODO: handle builtins!
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if ((known == null) || known.contains(null)) {
			throw new NullPointerException(
					"The known set must not be, or contain null");
		}

		final List<ILiteral> body = new ArrayList<ILiteral>(r.getBody());
		final Set<IVariable> bound = new HashSet<IVariable>(known);
		for (int i = 0, max = body.size(); i < max; i++) {
			final ILiteral l = body.get(i);
			if (!l.isPositive()) {
				// check whether every var appears in a former literal
				if (!bound.containsAll(l.getAtom().getTuple().getVariables())) {
					// if a variable can't be found -> put literal at the end
					body.add(body.size() - 1, body.remove(i));
					i--;
				} else { // consider the vars as already bound
					bound.addAll(l.getAtom().getTuple().getVariables());
				}
			} else { // consider the vars as already bound
				bound.addAll(l.getAtom().getTuple().getVariables());
			}
		}
		return Factory.BASIC.createRule(r.getHead(), body);
	}

	/**
	 * Comparator to compare two literals to each other depending on their
	 * position in the sip.</br>If one literal is smaller than the other this
	 * means that it precedes the other one. </br>Following rules will be
	 * followed:</br>
	 * <ul>
	 * <li>if none of the literals is in the graph they are equal</li>
	 * <li>if a literal appears in the graph it is smaller than one which is
	 * not in the graph</li>
	 * <li>the literal which precedes another literal is smaller than the other</li>
	 * <uol>
	 * 
	 * @author richi
	 * 
	 */
	private class LiteralComparator implements Comparator<ILiteral> {

		/**
		 * Compares two literal to each other depending on their position in the
		 * sip.
		 * 
		 * @param o1
		 *            the first literal to compare
		 * @param 02
		 *            the second literal to compare
		 * @return -1, 0, 1 if o1 is smaller, equal or bitter than o2
		 * @throws NullPointerException
		 *             if o1 or o2 is null
		 */
		public int compare(final ILiteral o1, final ILiteral o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException();
			}

			if (!sipGraph.containsVertex(o1) && !sipGraph.containsVertex(o2)) {
				return 0;
			} else if (!sipGraph.containsVertex(o1)) {
				return 1;
			} else if (!sipGraph.containsVertex(o2)) {
				return -1;
			}
			if (getDepends(o2).contains(o1)) {
				return -1;
			}
			return 1;
		}
	}

	/**
	 * <p>
	 * The simple factory to create default edges for the Sip.
	 * The label of the edge will be <code>new HashSet<IVariable>()</code>.
	 * </p>
	 * <p>
	 * $Id: SIPImpl.java,v 1.28 2007-10-30 10:35:49 poettler_ric Exp $
	 * </p>
	 * @author Richard Pöttler (richard dot poettler at deri dot org)
	 * @version $Revision: 1.28 $
	 * @since 0.3
	 */
	private static class SipEdgeFactory implements EdgeFactory<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>> {

		public LabeledEdge<ILiteral, Set<IVariable>> createEdge(final ILiteral s, final ILiteral t) {
			if ((s == null) || (t == null)) {
				throw new NullPointerException("The vertices must not be null");
			}
			return new LabeledEdge<ILiteral, Set<IVariable>>(s, t, new HashSet<IVariable>());
		}
	}
}
