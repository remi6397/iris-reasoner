/*
 * Integrated Rule Inference System (IRIS): An extensible rule inference system
 * for datalog with extensions by built-in predicates, default negation (under
 * well-founded semantics), function symbols and contexts.
 * 
 * Copyright (C) 2006 Digital Enterprise Research Institute (DERI),
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, A-6020
 * Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.deri.iris.evaluation.magic;

import static org.deri.iris.factory.Factory.BASIC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.GraphHelper;
import org._3pq.jgrapht.graph.SimpleDirectedGraph;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IConstantTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.graph.LabeledDirectedEdge;

/**
 * A SIP Implementation according to the &quot;The Power of Magic&quot; paper.
 * </br></br>$Id: SIPImpl.java,v 1.6 2006-08-22 09:04:04 richardpoettler Exp $
 * 
 * @author richi
 * @version $Revision: 1.6 $
 */
public class SIPImpl {
	// TODO: implement hashCode and equals

	/** Comparator to compare literals according to their position in the sips. */
	public final Comparator<ILiteral> LITERAL_COMP = new LiteralComparator();

	/** The graph on which the variables are padded along. */
	private DirectedGraph sipGraph = new SimpleDirectedGraph();

	/** The rule which is represented by this sip. */
	private IRule rule = null;

	/** The query for wich the sip was created. */
	private IQuery query = null;

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
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only rule heads with length 1 are allowed");
		}
		if (q.getQueryLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only queries with length 1 are allowed");
		}
		// TODO: maybe make defendive copies
		rule = r;
		query = q;

		final ILiteral headLiteral = rule.getHeadLiteral(0);
		final ILiteral queryLiteral = query.getQueryLiteral(0);
		final IPredicate headPredicate = headLiteral.getPredicate();
		final IPredicate queryPredicate = queryLiteral.getPredicate();

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
		for (final Iterator<ITerm> headTerms =
				headLiteral.getTuple().getTerms().iterator(), queryTerms =
				queryLiteral.getTuple().getTerms().iterator(); (headTerms
				.hasNext() && queryTerms.hasNext());) {
			final ITerm headT = headTerms.next();
			final ITerm queryT = queryTerms.next();
			// FIXME: be aware of constructed terms
			if ((queryT instanceof IConstantTerm)
					&& (headT instanceof IVariable)) {
				assumedKnown.add((IVariable) headT);
			}
		}

		// constructing the sip
		final List<ILiteral> literalsTodo = new ArrayList<ILiteral>();
		final Comparator<ILiteral> passingOrder = new DefaultPassingOrder();
		literalsTodo.addAll(jumpFromHead(headLiteral, assumedKnown));

		while (!literalsTodo.isEmpty()) {
			Collections.sort(literalsTodo, passingOrder);
			final ILiteral l = literalsTodo.remove(0);

			final Map<ILiteral, Set<IVariable>> toAdd =
					getNextByFree(l, assumedKnown);
			for (ILiteral connected : toAdd.keySet()) {
				updateSip(l, connected, toAdd.get(connected));
				literalsTodo.add(connected);
			}
		}
		// FIXME: add unconnected literals
	}

	/**
	 * Updates the sip, so that it contains an edge from the source whitch
	 * passes the given variables to the target.
	 * 
	 * @param source
	 *            the source literal
	 * @param target
	 *            the literal the variables are passed to
	 * @param passedTo
	 *            the variables whitch are passed
	 * @return the edge
	 * @throws NullPointerException
	 *             if the source, target or set of variables is null
	 */
	private void updateSip(final ILiteral source, final ILiteral target,
			final Set<IVariable> passedTo) {
		if ((source == null) || (target == null) || (passedTo == null)) {
			throw new NullPointerException(
					"The source, target and passed variables must not be null");
		}
		GraphHelper.addEdgeWithVertices(sipGraph, constructEdge(source, target,
				passedTo));
	}

	/**
	 * Creates a labeled edge representing a variable passing from one literal
	 * to another.
	 * 
	 * @param source
	 *            the source literal
	 * @param target
	 *            the literal the variables are passed to
	 * @param passedTo
	 *            the variables whitch are passed
	 * @return the edge
	 * @throws NullPointerException
	 *             if the source, target or set of variables is null
	 */
	private LabeledDirectedEdge<Set<IVariable>> constructEdge(
			final ILiteral source, final ILiteral target,
			final Set<IVariable> passedTo) {
		if ((source == null) || (target == null) || (passedTo == null)) {
			throw new NullPointerException(
					"The source, target and passed variables must not be null");
		}
		return new LabeledDirectedEdge<Set<IVariable>>(source, target, passedTo);
	}

	/**
	 * Makes the first step in creating the sip, because the first one is
	 * different than the others. First this method will look for possibilities
	 * where to pass it's bound variables to, add the corresponding edges to the
	 * sip and returns a set of literals where new connections where established
	 * to.
	 * 
	 * @param l
	 *            the head literal
	 * @param bound
	 *            the set of variables, bound by the query
	 * @return the set of the next literals to process
	 * @throws NullPointerException
	 *             if the literal or the set is null
	 */
	private Set<ILiteral> jumpFromHead(final ILiteral l,
			final Set<IVariable> bound) {
		if ((l == null) || (bound == null)) {
			throw new NullPointerException(
					"The literal and the bounds must not be null");
		}
		Map<ILiteral, Set<IVariable>> next = getNextByBounds(l, bound);
		for (final ILiteral lit : next.keySet()) {
			updateSip(l, lit, next.get(lit));
		}
		return next.keySet();
	}

	/**
	 * Determines all variables, wich are passed to this literal.
	 * 
	 * @param l
	 *            the literal for which to determine the variables
	 * @return the set of variables
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public Set<IVariable> getBoundVariables(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException();
		}

		final Set<IVariable> variables = new HashSet<IVariable>();
		final List predecessors = GraphHelper.predecessorListOf(sipGraph, l);
		for (Object o : predecessors) {
			for (Object e : sipGraph.getAllEdges(o, l)) {
				variables.addAll(((LabeledDirectedEdge<Set<IVariable>>) e)
						.getLabel());
			}
		}
		return variables;
	}

	/**
	 * Searches for literals in the rule body whitch are connected to this one
	 * by the known variables. </br></br>This method returns a map containing
	 * literals as keys and a sets of variables as values. The literals are the
	 * literals which are connected to the given one and the variables are the
	 * variables of the connected literal limited to those of the &quot;known
	 * set&quot;.
	 * 
	 * @param l
	 *            for which to search for connected literals
	 * @param known
	 *            the set of known varibles of this literal
	 * @return the map of the connected literals and the variables which are
	 *         passed to it
	 * @throws NullPointerException
	 *             if the literal or the set is null
	 */
	private Map<ILiteral, Set<IVariable>> getNextByBounds(final ILiteral l,
			final Set<IVariable> known) {
		// FIXME: update the sip if literal is already in the sip
		if ((l == null) || (known == null)) {
			throw new NullPointerException(
					"The literal or the set of bounds must not be null");
		}

		final Map<ILiteral, Set<IVariable>> connected =
				new HashMap<ILiteral, Set<IVariable>>();

		final Set<IVariable> bounds = l.getTuple().getVariables();
		bounds.retainAll(known);
		if (bounds.isEmpty()) {
			return connected;
		}

		for (ILiteral lit : rule.getBodyLiterals()) {
			final Set<IVariable> variables =
					new HashSet<IVariable>(lit.getTuple().getVariables());
			variables.retainAll(bounds);
			if (!variables.isEmpty()) {
				connected.put(lit, variables);
			}
		}
		return connected;
	}

	/**
	 * Searches for literals in the rule body whitch are connected to this one
	 * by the free variables of this one. </br></br>This method returns a map
	 * containing literals as keys and a sets of variables as values. The
	 * literals are the literals which are connected to the given one and the
	 * variables are the variables of the connected literal limited to those of
	 * the &quot;known set&quot;.
	 * 
	 * @param l
	 *            for which to search for connected literals
	 * @param initiallyKnown
	 *            the set of known varibles of this literal
	 * @return the map of the connected literals and the variables which are
	 *         passed to it
	 * @throws NullPointerException
	 *             if the literal or the set is null
	 */
	private Map<ILiteral, Set<IVariable>> getNextByFree(final ILiteral l,
			final Set<IVariable> initiallyKnown) {
		if ((l == null) || (initiallyKnown == null)) {
			throw new NullPointerException("The literal must not be null");
		}

		final Map<ILiteral, Set<IVariable>> connected =
				new HashMap<ILiteral, Set<IVariable>>();

		// determine all possible successors of this literal
		final Set<ILiteral> possibleSuccessors =
				new HashSet<ILiteral>(rule.getBodyLiterals());

		possibleSuccessors.remove(l);

		// getting all unbound variables of this literal
		final Set<IVariable> unbound = l.getTuple().getVariables();
		unbound.removeAll(getBoundVariables(l));
		unbound.removeAll(initiallyKnown);

		for (final ILiteral possible : possibleSuccessors) {
			final Set<IVariable> commonVars =
					new HashSet<IVariable>(possible.getTuple().getVariables());
			commonVars.retainAll(unbound);
			if (!commonVars.isEmpty()) {
				connected.put(possible, commonVars);
			}
		}
		return connected;
	}

	/**
	 * Checks whether two predicates have the same signature. Same signature
	 * means equal predicate symbol and arity.
	 * 
	 * @param p1
	 *            the first predicate
	 * @param p2
	 *            the second predicate
	 * @return true if the signature of the predicates matches each other,
	 *         false, if not.
	 * @throws NullPointerException
	 *             if one of the predicates is null
	 */
	private static boolean hasSameSignature(final IPredicate p1,
			final IPredicate p2) {
		// TODO: maybe this method should be put in a static helper class
		if ((p1 == null) || (p2 == null)) {
			throw new NullPointerException("The predicates must not be null");
		}
		return p1.getPredicateSymbol().equals(p2.getPredicateSymbol())
				&& (p1.getArity() == p2.getArity());
	}

	/**
	 * Searches for literals on which the submitted literal depends.
	 * 
	 * @param l
	 *            the literal for which to search for dependencies
	 * @return the set of literal on which the submitted literal depends
	 * @throws NullPointerException
	 *             if the literal is null
	 */
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

			final List vertices =
					GraphHelper.predecessorListOf(sipGraph, actual);
			dependencies.addAll(vertices);
			todoDependencies.addAll(vertices);
		}

		return dependencies;
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
		for (Object o : sipGraph.edgeSet()) {
			buffer.append(o).append(NEWLINE);
		}
		return buffer.toString();
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

			final ILiteral l1 = getUnadornedLiteral(o1);
			final ILiteral l2 = getUnadornedLiteral(o2);

			if (!sipGraph.containsVertex(l1) && !sipGraph.containsVertex(l2)) {
				return 0;
			} else if (!sipGraph.containsVertex(l1)) {
				return 1;
			} else if (!sipGraph.containsVertex(l2)) {
				return -1;
			}
			if (getDepends(l2).contains(l1)) {
				return -1;
			}
			return 1;
		}

		/**
		 * Returns a literal with an unadorned predicate for one which might be
		 * adorned.
		 * 
		 * @param l
		 *            the literal which might be adorned
		 * @return the literal with an unadorned predicate
		 * @throws NullPointerException
		 *             if the literal is null
		 */
		private ILiteral getUnadornedLiteral(final ILiteral l) {
			if (l == null) {
				throw new NullPointerException();
			}
			if (l.getPredicate() instanceof AdornedPredicate) {
				return BASIC.createLiteral(l.isPositive(),
						((AdornedPredicate) l.getPredicate())
								.getUnadornedPredicate(), l.getTuple());
			}
			return l;
		}
	}

	/**
	 * Compares literals according to some heuristics.
	 * 
	 * @author richi
	 * @deprecated because the heuristics aren't well defined at the moment.
	 */
	private class HeuristicPassingOrder implements Comparator<ILiteral> {

		private final Set<IVariable> freeByQuery = new HashSet<IVariable>();

		private final IPredicate headPredicate;

		public HeuristicPassingOrder() {
			final ILiteral headLiteral = rule.getHeadLiteral(0);
			final ILiteral queryLiteral = query.getQueryLiteral(0);

			headPredicate = headLiteral.getPredicate();

			for (final Iterator<ITerm> headTerms =
					headLiteral.getTuple().getTerms().iterator(), queryTerms =
					queryLiteral.getTuple().getTerms().iterator(); (headTerms
					.hasNext() && queryTerms.hasNext());) {
				final ITerm headT = headTerms.next();
				final ITerm queryT = queryTerms.next();
				// FIXME: be aware of constructed terms
				if ((queryT instanceof IVariable)
						&& (headT instanceof IVariable)) {
					freeByQuery.add((IVariable) headT);
				}
			}
		}

		public int compare(final ILiteral o1, final ILiteral o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The literals must not be null");
			}
			int result = 0;
			if ((result = compareContainsFreeQuery(o1, o2)) != 0) {
				return result;
			}
			if ((result = compareIsEdb(o1, o2)) != 0) {
				return result;
			}
			return result;
		}

		private int compareContainsFreeQuery(final ILiteral o1,
				final ILiteral o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The literals must not be null");
			}

			final Set<IVariable> v1 = o1.getTuple().getVariables();
			final Set<IVariable> v2 = o2.getTuple().getVariables();
			v1.retainAll(freeByQuery);
			v2.retainAll(freeByQuery);

			if (!v1.isEmpty() && !v2.isEmpty()) {
				return v1.size() - v2.size();
			} else if (v1.isEmpty() && !v2.isEmpty()) {
				return -1;
			} else if (!v1.isEmpty() && v2.isEmpty()) {
				return 1;
			}
			return 0;
		}

		private int compareIsEdb(final ILiteral o1, final ILiteral o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The literals must not be null");
			}

			final IPredicate p1 = o1.getPredicate();
			final IPredicate p2 = o2.getPredicate();
			if (hasSameSignature(p1, headPredicate)
					&& !hasSameSignature(p2, headPredicate)) {
				return 1;
			} else if (!hasSameSignature(p1, headPredicate)
					&& hasSameSignature(p2, headPredicate)) {
				return -1;
			}
			return 0;
		}
	}

	/**
	 * Compares literals according to their position in the body of the rule.
	 * Literals appearing earlier in the body are smaller than those later in
	 * the body. If a literal doesn't appear in the body, then it is bigger than
	 * one whitch is in the body. If both aren't in the body, they are equal.
	 * 
	 * @author richi
	 */
	private class DefaultPassingOrder implements Comparator<ILiteral> {
		public int compare(ILiteral o1, ILiteral o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The literals must not be null");
			}
			final int pos1 = rule.getBodyLiterals().indexOf(o1);
			final int pos2 = rule.getBodyLiterals().indexOf(o2);
			if ((pos1 == -1 && pos2 == -1)) {
				return 0;
			} else if (pos1 == -1) {
				return 1;
			} else if (pos2 == -1) {
				return -1;
			}
			return pos1 - pos2;
		}
	}
}
