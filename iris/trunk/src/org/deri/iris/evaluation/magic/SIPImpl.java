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
import org.deri.iris.api.evaluation.magic.ISip;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.graph.LabeledEdge;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleDirectedGraph;

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
 * $Id: SIPImpl.java,v 1.22 2007-10-14 14:49:00 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.22 $
 */
public final class SIPImpl implements ISip {
	/**
	 * Comparator to compare literals according to their position in the sips.
	 */
	private final Comparator<ILiteral> LITERAL_COMP = new LiteralComparator();

	/** The graph on which the variables are padded along. */
	private DirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>> sipGraph = 
		new SimpleDirectedGraph<ILiteral, LabeledEdge<ILiteral, Set<IVariable>>>(new SipEdgeFactory());

	/** The rule which is represented by this sip. */
	private IRule rule = null;

	/** The query for wich the sip was created. */
	private IQuery query = null;

	private SIPImpl() {
	}

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
		if (r.getHead().getLength() != 1) {
			throw new IllegalArgumentException(
					"At the moment only rule heads with length 1 are allowed");
		}
		if (q.getQueryLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only queries with length 1 are allowed");
		}
		// TODO: maybe make defensive copies
		rule = r;
		query = q;

		final ILiteral headLiteral = rule.getHead().getLiteral(0);
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
		for (final Iterator<ITerm> headTerms = headLiteral.getTuple()
				.getTerms().iterator(), queryTerms = queryLiteral.getTuple()
				.getTerms().iterator(); (headTerms.hasNext() && queryTerms
				.hasNext());) {
			final ITerm headT = headTerms.next();
			final ITerm queryT = queryTerms.next();
			if (queryT.isGround() && !headT.isGround()) {
				// FIXME: this might not always be a variable, use
				// getVariables()
				assumedKnown.add((IVariable) headT);
			}
		}
		
		// ensure that a save rule will result in a save sip
		rule = orderLiterals(rule, assumedKnown);

		// constructing the sip
		final List<ILiteral> literalsTodo = new ArrayList<ILiteral>();
		final Comparator<ILiteral> passingOrder = new DefaultPassingOrder();
		literalsTodo.addAll(jumpFromHead(headLiteral, assumedKnown));

		while (!literalsTodo.isEmpty()) {
			Collections.sort(literalsTodo, passingOrder);
			final ILiteral l = literalsTodo.remove(0);

			final Map<ILiteral, Set<IVariable>> toAdd = getNextByRule(l,
					assumedKnown);
			for (final ILiteral connected : toAdd.keySet()) {
				assumedKnown.addAll(toAdd.get(connected));
				updateSip(l, connected, toAdd.get(connected));
				literalsTodo.add(connected);
			}
		}
		// FIXME: add unconnected literals
	}

	/**
	 * Updates the sip. Adds a edge with the given source, target and given
	 * label to the sip. If a edge with the given source and target already
	 * exists, the label will be updated.
	 * 
	 * @throws NullPointerException
	 *             if the source, target or passedTo is {@code null}
	 */
	public void updateSip(final ILiteral source, final ILiteral target,
			final Set<IVariable> passedTo) {
		if ((source == null) || (target == null) || (passedTo == null)) {
			throw new NullPointerException(
					"The source, target and passed variables must not be null");
		}
		if (sipGraph.containsEdge(source, target)) { // updating the edge
			sipGraph.getEdge(source, target).getLabel().addAll(passedTo);
		}
		// adding a new edge
		sipGraph.addVertex(source);
		sipGraph.addVertex(target);
		sipGraph.addEdge(source, target, new LabeledEdge<ILiteral, Set<IVariable>>(source, target, passedTo));
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
		final Map<ILiteral, Set<IVariable>> next = getNextByRule(l, bound);
		for (final ILiteral lit : next.keySet()) {
			final Set<IVariable> vars = new HashSet<IVariable>(next.get(lit));
			vars.retainAll(bound);
			updateSip(l, lit, vars);
		}
		return next.keySet();
	}

	public Set<IVariable> getBoundVariables(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException();
		}

		final Set<IVariable> variables = new HashSet<IVariable>();
		for (final ILiteral o : Graphs.predecessorListOf(sipGraph, l)) {
			for (LabeledEdge<ILiteral, Set<IVariable>> e : sipGraph.getAllEdges(o, l)) {
				variables.addAll(e.getLabel());
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
		// FIXME: builtins are handeled as ordinary literals
		if ((l == null) || (known == null)) {
			throw new NullPointerException(
					"The literal or the set of bounds must not be null");
		}

		final Map<ILiteral, Set<IVariable>> connected = new HashMap<ILiteral, Set<IVariable>>();

		final Set<IVariable> bounds = l.getTuple().getVariables();
		bounds.retainAll(known);
		if (bounds.isEmpty()) {
			return connected;
		}

		for (ILiteral lit : rule.getBody().getLiterals()) {
			final Set<IVariable> variables = new HashSet<IVariable>(lit
					.getTuple().getVariables());
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
		// FIXME: builtins are handeled as ordinary literals
		if ((l == null) || (initiallyKnown == null)) {
			throw new NullPointerException("The literal must not be null");
		}

		final Map<ILiteral, Set<IVariable>> connected = new HashMap<ILiteral, Set<IVariable>>();

		// determine all possible successors of this literal
		final Set<ILiteral> possibleSuccessors = new HashSet<ILiteral>(rule
				.getBody().getLiterals());

		possibleSuccessors.remove(l);

		// getting all unbound variables of this literal
		final Set<IVariable> unbound = l.getTuple().getVariables();
		unbound.removeAll(getBoundVariables(l));
		unbound.removeAll(initiallyKnown);

		for (final ILiteral possible : possibleSuccessors) {
			final Set<IVariable> commonVars = new HashSet<IVariable>(possible
					.getTuple().getVariables());
			commonVars.retainAll(unbound);
			if (!commonVars.isEmpty()) {
				connected.put(possible, commonVars);
			}
		}
		return connected;
	}

	/**
	 * This simply acts like the stupid default sip. It takes simply the next
	 * literal in the rule. <b>If there are two literals equal to the given one
	 * in the rule body it will always return the element one after the first
	 * occurence of the given literal.</b>
	 * 
	 * @param l
	 *            for which to search for connected literals
	 * @param initiallyKnown
	 *            the set of known varibles of this literal
	 * @return the map of the connected literals and the variables which are
	 *         passed to it
	 * @throws NullPointerException
	 *             if the literal or the set is {@code null}
	 */
	private Map<ILiteral, Set<IVariable>> getNextByRule(final ILiteral l,
			final Set<IVariable> initiallyKnown) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		final Set<IVariable> vars = new HashSet<IVariable>(initiallyKnown);
		vars.addAll(l.getTuple().getVariables());
		if (l.equals(rule.getHead().getLiteral(0))) {
			return Collections.singletonMap(rule.getBody().getLiteral(0), vars);
		}

		// get the next literal -> ignore builtins
		// FIXME: if two equal literals are in the body -> infinite loop
		int newPos = rule.getBody().getLiterals().indexOf(l) + 1;
		final int rLength = rule.getBody().getLength();
		if (newPos >= rLength) {
			return Collections.EMPTY_MAP;
		}
		return Collections.singletonMap(rule.getBody().getLiteral(newPos), vars);
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

	public IRule getRule() {
		return rule;
	}

	public IQuery getQuery() {
		return query;
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

	public ISip defensifeCopy() {
		return defensifeCopy(rule, query);
	}

	public SIPImpl defensifeCopy(final IRule r, final IQuery q) {
		if ((r == null) || (q == null)) {
			throw new NullPointerException(
					"The rule and the query must not be null");
		}
		// TODO: maybe check the predicates of the query and the rulehead
		SIPImpl copy = new SIPImpl();
		copy.rule = r;
		copy.query = q;
		copy.sipGraph = (DirectedGraph)((SimpleDirectedGraph) sipGraph).clone();
		return copy;
	}

	public void exchangeLiteral(final ILiteral from, final ILiteral to) {
		if ((from == null) || (to == null)) {
			throw new NullPointerException("The literals must not be null");
		}
		for (final LabeledEdge<ILiteral, Set<IVariable>> e : getEdgesEnteringLiteral(from)) {
			updateSip(e.getSource(), to, e.getLabel());
		}
		for (final LabeledEdge<ILiteral, Set<IVariable>> e : getEdgesLeavingLiteral(from)) {
			updateSip(to, e.getTarget(), e.getLabel());
		}
		// needs no check for neighbours, because we deleted all edges
		sipGraph.removeVertex(from);
	}

	public void removeEdge(final LabeledEdge<ILiteral, Set<IVariable>> e) {
		if (e == null) {
			throw new NullPointerException("The edge must not be null");
		}
		sipGraph.removeEdge(e);
	}

	public void removeEdge(final ILiteral source, final ILiteral target) {
		if ((source == null) || (target == null)) {
			throw new NullPointerException(
					"The source and the target must not be null");
		}
		removeEdge(sipGraph.getEdge(source, target));
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
		return query.equals(s.query) && rule.equals(s.rule)
				&& sipGraph.edgeSet().equals(s.sipGraph.edgeSet());
	}

	public int hashCode() {
		int res = 17;
		res = res * 37 + rule.hashCode();
		res = res * 37 + query.hashCode();
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

		final List<ILiteral> body = new ArrayList<ILiteral>(r.getBody().getLiterals());
		final Set<IVariable> bound = new HashSet<IVariable>(known);
		for (int i = 0, max = body.size(); i < max; i++) {
			final ILiteral l = body.get(i);
			if (!l.isPositive()) {
				// check whether every var appears in a former literal
				if (!bound.containsAll(l.getTuple().getVariables())) {
					// if a variable can't be found -> put literal at the end
					body.add(body.size() - 1, body.remove(i));
					i--;
				} else { // consider the vars as already bound
					bound.addAll(l.getTuple().getVariables());
				}
			} else { // consider the vars as already bound
				bound.addAll(l.getTuple().getVariables());
			}
		}
		return Factory.BASIC.createRule(Factory.BASIC.createHead(r
				.getHead().getLiterals()), Factory.BASIC.createBody(body));
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
	 * Compares literals according to some heuristics.
	 * 
	 * @author richi
	 * @deprecated because the heuristics aren't well defined at the moment.
	 */
	private class HeuristicPassingOrder implements Comparator<ILiteral> {

		private final Set<IVariable> freeByQuery = new HashSet<IVariable>();

		private final IPredicate headPredicate;

		public HeuristicPassingOrder() {
			final ILiteral headLiteral = rule.getHead().getLiteral(0);
			final ILiteral queryLiteral = query.getQueryLiteral(0);

			headPredicate = headLiteral.getPredicate();

			for (final Iterator<ITerm> headTerms = headLiteral.getTuple()
					.getTerms().iterator(), queryTerms = queryLiteral
					.getTuple().getTerms().iterator(); (headTerms.hasNext() && queryTerms
					.hasNext());) {
				final ITerm headT = headTerms.next();
				final ITerm queryT = queryTerms.next();
				if (!queryT.isGround() && !headT.isGround()) {
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
			final int pos1 = rule.getBody().getLiterals().indexOf(o1);
			final int pos2 = rule.getBody().getLiterals().indexOf(o2);
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

	/**
	 * <p>
	 * The simple factory to create default edges for the Sip.
	 * The label of the edge will be <code>new HashSet<IVariable>()</code>.
	 * </p>
	 * <p>
	 * $Id: SIPImpl.java,v 1.22 2007-10-14 14:49:00 bazbishop237 Exp $
	 * </p>
	 * @author Richard Pöttler (richard dot poettler at deri dot org)
	 * @version $Revision: 1.22 $
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
