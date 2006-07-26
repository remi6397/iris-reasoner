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
package org.deri.iris.evaluation.magic;

import java.util.Collections;
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
import org.deri.iris.graph.LabeledDirectedEdge;

/**
 * This is a simple implementation of sip's.
 * <br/><br/>$Id: SIPImpl.java,v 1.3 2006-07-20 09:02:51 richardpoettler Exp $
 * 
 * @author richi
 * @version $Revision: 1.3 $
 * @date $Date: 2006-07-20 09:02:51 $
 */
public class SIPImpl {
	// TODO: implement hashCode and equals

	/** The graph on which the variables are padded along */
	private DirectedGraph sipGraph = new SimpleDirectedGraph();

	/**
	 * A map for all the possible substitutions (at the moment not used)
	 */
	private Map<IVariable, Set<ITerm>> substitutions = new HashMap<IVariable, Set<ITerm>>();

	/** The rule which is represented by this sip */
	private IRule rule = null;

	/** The query for wich the sip was created */
	private IQuery query = null;

	/**
	 * Creates a SIP for the given rule with bindings for the given query<br/>
	 * NOTE: at the moment only the first literal of the head and the query are
	 * recognized.
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

		rule = r;
		query = q;

		Set<IVariable> assumedKnown = new HashSet<IVariable>();
		Set<ILiteral> literalsToDo = new HashSet<ILiteral>();
		ILiteral actualLiteral = null;

		ILiteral headLiteral = r.getHeadLiteral(0);
		ILiteral queryLiteral = q.getQueryLiteral(0);
		IPredicate headPredicate = headLiteral.getPredicate();
		IPredicate queryPredicate = queryLiteral.getPredicate();

		// this check is only neccessary for the first implementation
		// allow only one head
		// head predicate must be equals with the query predicate

		// I'm not using equals, because the query could be adorned, and then
		// they won't be equal
		if (!headPredicate.getPredicateSymbol().equals(
				queryPredicate.getPredicateSymbol())
				|| (headPredicate.getArity() != queryPredicate.getArity())) {
			throw new IllegalArgumentException("With this implementation the "
					+ "head and the query predicates must be the same");
		}

		// registring the variables
		for (ILiteral l : r.getHeadLiterals()) {
			for (Object t : l.getTerms()) {
				if ((t instanceof IVariable) && (!substitutions.containsKey(t))) {
					substitutions.put((IVariable) t, new HashSet<ITerm>());
				}
			}
		}
		for (ILiteral l : r.getBodyLiterals()) {
			for (Object t : l.getTerms()) {
				if ((t instanceof IVariable) && (!substitutions.containsKey(t))) {
					substitutions.put((IVariable) t, new HashSet<ITerm>());
				}
			}
		}

		// binding the first variable submitted by the query
		Iterator ruleTerms = headLiteral.getTerms().iterator();
		Iterator queryTerms = queryLiteral.getTerms().iterator();
		while (ruleTerms.hasNext() && queryTerms.hasNext()) {
			Object ruleT = ruleTerms.next();
			Object queryT = queryTerms.next();
			if (queryT instanceof IConstantTerm) {
				substitutions.get(ruleT).add(
						(ITerm) ((IConstantTerm) queryT).getValue());
			}
		}

		// adding all literals to the graph
		sipGraph.addAllVertices(rule.getBodyLiterals());

		// doint the first step -> putting in the head
		literalsToDo.addAll(jumpFromHeadLiteral(headLiteral));

		while (!literalsToDo.isEmpty()) {
			actualLiteral = literalsToDo.iterator().next();
			literalsToDo.remove(actualLiteral);

			// searching for connected literals to the actualLiteral
			Map<ILiteral, Set<IVariable>> connectedLiterals = getConnectedLiterals(
					actualLiteral, assumedKnown);

			for (ILiteral l : connectedLiterals.keySet()) {
				// adding the vertex and the edge with the label to the sipGraph
				sipGraph.addEdge(new LabeledDirectedEdge<Set<IVariable>>(
						actualLiteral, l, connectedLiterals.get(l)));
				// updating the variabled assumed to be know and the literals to
				// do.
				assumedKnown.addAll(connectedLiterals.get(l));
				literalsToDo.add(l);
			}
		}
	}

	/**
	 * This is a workaround, because the jump from the head literal to the
	 * bodyliterals is different (the bound terms are passed to the body).
	 * 
	 * @param hl
	 *            the head literal from which to create the first edge
	 * @return a set of literals to which the head literal is connected
	 */
	private Set<ILiteral> jumpFromHeadLiteral(final ILiteral hl) {
		IPredicate headPredicate = hl.getPredicate();
		IPredicate queryPredicate = query.getQueryLiteral(0).getPredicate();
		if (headPredicate.getPredicateSymbol().equals(
				queryPredicate.getPredicateSymbol())
				&& (headPredicate.getArity() != queryPredicate.getArity())) {
			throw new IllegalArgumentException("The predicates doesn't match");
		}

		Set<IVariable> unbound = new HashSet<IVariable>();

		sipGraph.addVertex(hl);

		// TODO: this could maybe optimized taking the variables from the
		// substitutions
		// determining the unbound variables -> ignore them when searching for
		// connected literals
		Iterator<ITerm> headTerms = hl.getTerms().iterator();
		Iterator<ITerm> queryTerms = query.getQueryLiteral(0).getTerms()
				.iterator();
		while (headTerms.hasNext() && queryTerms.hasNext()) {
			ITerm qt = queryTerms.next();
			ITerm ht = headTerms.next();
			if ((qt instanceof IVariable) && (ht instanceof IVariable)) {
				unbound.add((IVariable) ht);
			}
		}

		Map<ILiteral, Set<IVariable>> connected = getConnectedLiterals(hl,
				unbound);
		for (ILiteral l : connected.keySet()) {
			sipGraph.addEdge(new LabeledDirectedEdge<Set<IVariable>>(hl, l,
					connected.get(l)));
		}
		return connected.keySet();
	}

	/**
	 * Determines all variables, wich are passed to this literal.
	 * 
	 * @param l
	 *            the literal for which to determine the variables
	 * @return the set of variables
	 * @throws if
	 *             the literal is null
	 */
	public Set<IVariable> getBoundVariables(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException();
		}
		Set<IVariable> variables = new HashSet<IVariable>();
		List predecessors = GraphHelper.predecessorListOf(sipGraph, l);
		for (Object o : predecessors) {
			for (Object e : sipGraph.getAllEdges(o, l)) {
				variables.addAll(((LabeledDirectedEdge<Set<IVariable>>) e)
						.getLabel());
			}
		}
		return variables;
	}

	/**
	 * This method finds literals which are connected to the given one. It
	 * ignores literals already in the graph and literals which would be
	 * connected, but only though a variables which is equal with one in the
	 * ignore set.<br/>This method returns a map witch the connected literal
	 * as key, and with all connected variables as values.
	 * 
	 * @param l
	 *            the literal for which to check for connections
	 * @param ignore
	 *            a set of variables to ignore. If ignore is null, it will be
	 *            handeled as a empty set.
	 * @return the result map
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	private Map<ILiteral, Set<IVariable>> getConnectedLiterals(
			final ILiteral l, final Set<IVariable> ignore) {
		final Set<IVariable> toIgnore;
		final Map<ILiteral, Set<IVariable>> connectedLiterals = new HashMap<ILiteral, Set<IVariable>>();
		final Set<IVariable> freeVariables = new HashSet<IVariable>();

		if (l == null) {
			throw new NullPointerException();
		}
		if (ignore == null) {
			toIgnore = Collections.EMPTY_SET;
		} else {
			toIgnore = ignore;
		}

		// searching for variables which are assumed not to be bound
		for (Object o : l.getTerms()) {
			if ((o instanceof IVariable) && (!toIgnore.contains(o))) {
				freeVariables.add((IVariable) o);
			}
		}
		// searching for connected literals
		for (ILiteral bodyL : rule.getBodyLiterals()) {
			if (!sipGraph.containsVertex(bodyL)) {
				for (Object o : bodyL.getTerms()) {
					if (freeVariables.contains(o)) {
						if (connectedLiterals.containsKey(bodyL)) {
							connectedLiterals.get(bodyL).add((IVariable) o);
						} else {
							Set<IVariable> connectedVariables = new HashSet<IVariable>();
							connectedVariables.add((IVariable) o);
							connectedLiterals.put(bodyL, connectedVariables);
						}
					}
				}
			}
		}
		return connectedLiterals;
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
			ILiteral actual = todoDependencies.iterator().next();
			todoDependencies.remove(actual);

			List vertices = GraphHelper.predecessorListOf(sipGraph, actual);
			dependencies.addAll(vertices);
			todoDependencies.addAll(vertices);
		}

		return dependencies;
	}

	/**
	 * Returns a simple string representation of this graph. <b>The subject of
	 * the returned string is to change.</b> The returned string may be a list
	 * of all edges of this graph separated by newlines.
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
}
