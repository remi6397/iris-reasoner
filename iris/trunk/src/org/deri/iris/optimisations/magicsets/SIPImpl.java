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
package org.deri.iris.optimisations.magicsets;

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
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

import org.deri.iris.factory.Factory;
import org.deri.iris.graph.LabeledEdge;
import org.deri.iris.optimisations.magicsets.AdornedProgram.AdornedPredicate;
import org.deri.iris.optimisations.magicsets.Adornment;
import org.deri.iris.optimisations.magicsets.ISip;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;

// FIXME: most Set<IVariable> should be Set<ITerm> and contain !isGround() terms

/**
 * <p>
 * A SIP Implementation according to the &quot;The Power of Magic&quot; paper.
 * </p>
 * <p>
 * This class is final, because the constructor is calling public non-final
 * methods.
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at sti dot at)
 */
public final class SIPImpl implements ISip {
	/**
	 * Comparator to compare literals according to their position in the sips.
	 */
	private final Comparator<ILiteral> LITERAL_COMP = new LiteralComparator();

	/** The graph on which the variables are passed along. */
	private DirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>> sipGraph = 
		new DefaultDirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>>(new SipEdgeFactory());

	/**
	 * Creates a SIP for the given rule with bindings for the given query.<b>
	 * NOTE: at the moment only the first literal of the head and the query are
	 * recognized.</b>
	 * 
	 * @param r the rule for which to construct the graph
	 * @param q the query for this rule
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 * @throws IllegalArgumentException if the query is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the symbol and the arity of the head of the rule and the
	 *             query doesn't match
	 */
	public SIPImpl(final IRule r, final IQuery q) {
		if (r == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}
		if (q == null) {
			throw new IllegalArgumentException("The query must not be null");
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
		// head predicate must be equal to with the query predicate

		// I'm not using IPredicate.equals(...), because the query could
		// be adorned, and then they won't be equal, so I compare them
		// on the predicate symbol and arity.
		if (!headPredicate.getPredicateSymbol().equals(
				queryPredicate.getPredicateSymbol())
				|| (headPredicate.getArity() != queryPredicate.getArity())) {
			throw new IllegalArgumentException(
					"The symbol and arity of the predicates of "
							+ "the rule head and the query must match");
		}

		// determining the known variables of the head
		final Set<IVariable> assumedKnown = new HashSet<IVariable>();
		final ITuple headTuple = headLiteral.getAtom().getTuple();
		final ITuple queryTuple = queryLiteral.getAtom().getTuple();
		for (int i = 0, arity = headTuple.size(); i < arity; i++) {
			if (queryTuple.get(i).isGround() && !headTuple.get(i).isGround()) {
				assumedKnown.addAll(getVariables(headTuple.get(i)));
			}
		}

		// ensure that a safe rule will result in a safe sip and
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
	 * @param t the term for which to return the variables
	 * @return the set of variables in the term
	 */
	private static Set<IVariable> getVariables(final ITerm t) {
		if ((t == null) || t.isGround()) {
			return Collections.<IVariable>emptySet();
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
	 * Updates the sip. Adds an edge with the given source, target and label
	 * to the sip. If an edge with the given source and target already
	 * exists, the label will be updated.
	 */
	private void addEdge(final ILiteral source, final ILiteral target,
			final Set<IVariable> passedTo) {
		assert source != null: "The source must not be null";
		assert target != null: "The target must not be null";
		assert passedTo != null: "The passed variables must not be null";

		final LabeledEdge<ILiteral, Set<IVariable>> edge = sipGraph.getEdge(source, target);
		if (edge != null) { // updating the edge
			edge.getLabel().addAll(passedTo);
		} else { // adding a new edge with the passings
			// adding the the literals as vertices
			sipGraph.addVertex(source);
			sipGraph.addVertex(target);

			// add the edge
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

		for (final ILiteral predicate : Graphs.predecessorListOf(sipGraph, l)) {
			variables.addAll(sipGraph.getEdge(predicate, l).getLabel());
		}
		return variables;
	}

	public Set<ILiteral> getDepends(final ILiteral l) {
		final Set<ILiteral> dependencies = new HashSet<ILiteral>();
		final Set<ILiteral> todoDependencies = new HashSet<ILiteral>();
		if (l == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		todoDependencies.add(l);
		while (!todoDependencies.isEmpty()) {
			final ILiteral actual = todoDependencies.iterator().next();
			todoDependencies.remove(actual);

			final List<ILiteral> vertices = Graphs.predecessorListOf(sipGraph,
					actual);
			for (final ILiteral lit : vertices) {
				if (dependencies.add(lit)) {
					todoDependencies.add(lit);
				}
			}
		}

		return dependencies;
	}

	public Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdgesEnteringLiteral(
			final ILiteral l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal must not be null");
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
			throw new IllegalArgumentException("The literal must not be null");
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
			throw new IllegalArgumentException(
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
			throw new IllegalArgumentException("The literal must not be null");
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
		final Set<ILiteral> leaves = new HashSet<ILiteral>();
		for (final ILiteral o : sipGraph.vertexSet()) {
			if (Graphs.successorListOf(sipGraph, o).isEmpty()) {
				leaves.add(o);
			}
		}
		return leaves;
	}

	public Comparator<ILiteral> getLiteralComparator() {
		return LITERAL_COMP;
	}

	/**
	 * Returns an unmodifiable set of all edges of the sip.
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
	 * Orders the literals of the rule so that a safe rule won't produce an
	 * unsafe sip. <b>At the moment only negative literals are handled, but not
	 * built-is.</b>
	 * 
	 * @param r the rule for which to sort the literals
	 * @param known the variables which are known in the head
	 * @throws IllegalArgumentException if the rule is {@code null}
	 * @throws IllegalArgumentException if the known collection is or contains
	 * {@code null}
	 */
	static IRule orderLiterals(final IRule r,
			final Collection<IVariable> known) {
		// TODO: handle builtins!
		if (r == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}
		if ((known == null) || known.contains(null)) {
			throw new IllegalArgumentException(
					"The known set must not be, or contain null");
		}

		final List<ILiteral> body = new ArrayList<ILiteral>(r.getBody());
		final Set<IVariable> bound = new HashSet<IVariable>(known);
		ILiteral firstPushedBack = null;
		int pushBackCount = 0;
		for (int i = 0, max = body.size(); i < max; i++) {
			final ILiteral l = body.get(i);
			final Set<IVariable> literalVars = l.getAtom().getTuple().getVariables();
			// if we meet the first pushed back literal again, we
			// were in a loop of only pushing back literals, without
			// any gains of bound variables -> stop now
			if (l.equals(firstPushedBack) && ((i + pushBackCount) >= (max - 1))) {
				break;
			}
			// if the literal is negative check whether every var
			// appears in a preceding literal -> if not, push the
			// literal at the end of the rule
			if (!l.isPositive() && !bound.containsAll(literalVars)) {
				body.add(body.size() - 1, body.remove(i));
				i--;

				pushBackCount++;
				if (firstPushedBack == null) {
					firstPushedBack = l;
				}
			} else { // consider the vars as already bound
				bound.addAll(literalVars);
				pushBackCount = 0;
				firstPushedBack = null;
			}
		}
		return Factory.BASIC.createRule(r.getHead(), body);
	}

	/**
	 * <p>
	 * Comparator to compare two literals with each other depending on their
	 * position in the sip.
	 * </p>
	 * <p>
	 * If one literal is smaller than the other this means that it precedes
	 * the other. Following rules will be applied:
	 * <ol>
	 * <li>If none of the literals is in the graph they are equal</li>
	 * <li>If only one literal appears in the graph it is smaller than one
	 * not in the graph</li>
	 * <li>The literal preceding the other is smaller</li>
	 * </ol>
	 * </p>
	 * 
	 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
	 */
	private class LiteralComparator implements Comparator<ILiteral> {

		public int compare(final ILiteral o1, final ILiteral o2) {
			if (o1 == null) {
				throw new IllegalArgumentException("The first literal must not be null");
			}
			if (o2 == null) {
				throw new IllegalArgumentException("The second literal must not be null");
			}

			if (!sipGraph.containsVertex(o1) && !sipGraph.containsVertex(o2)) { // none of the literals is in the graph
				return 0;
			} else if (!sipGraph.containsVertex(o1)) { // only o2 is in the graph
				return 1;
			} else if (!sipGraph.containsVertex(o2)) { // only o1 is in the graph
				return -1;
			}

			final boolean o1DependsOnO2 = getDepends(o1).contains(o2);
			final boolean o2DependsOnO1 = getDepends(o2).contains(o1);

			if (o1DependsOnO2 && o2DependsOnO1) { // they depend on each other
				return 0;
			} else if (o1DependsOnO2) { // o1 depends on o2
				return 1;
			} else if (o2DependsOnO1) { // o2 depends on o1
				return -1;
			}
			// they don't depend on each other
			return 0;
		}
	}

	/**
	 * <p>
	 * The simple factory to create default edges for the Sip.
	 * The label of the edge will be <code>new HashSet<IVariable>()</code>.
	 * </p>
	 *
	 * @author Richard Pöttler (richard dot poettler at sti dot at)
	 * @since 0.3
	 */
	private static class SipEdgeFactory implements EdgeFactory<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>> {

		public LabeledEdge<ILiteral, Set<IVariable>> createEdge(final ILiteral s, final ILiteral t) {
			if (s == null) {
				throw new IllegalArgumentException("The source must not be null");
			}
			if (t == null) {
				throw new IllegalArgumentException("The target must not be null");
			}
			return new LabeledEdge<ILiteral, Set<IVariable>>(s, t, new HashSet<IVariable>());
		}
	}
}
