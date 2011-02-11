/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.optimisations.magicsets;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.optimisations.magicsets.AdornedProgram.AdornedPredicate;

import eu.soa4all.graph.Direction;
import eu.soa4all.graph.Edge;
import eu.soa4all.graph.Graph;
import eu.soa4all.graph.Graphs;
import eu.soa4all.graph.impl.GraphFactory;

/**
 * <p>
 * Sip, which examines the body literals from left to right.
 * </p>
 * <p>
 * This class is final, because the constructor is calling public non-final
 * methods.
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
 */
public class LeftToRightSip implements ISip {
	/**
	 * Comparator to compare literals according to their position in the sips.
	 */
	private final Comparator<ILiteral> LITERAL_COMPARATOR = new LiteralComparator();

	/**
	 * Indicator flag since a directed graph is needed
	 */
	private final boolean directed = true;
	
	/**
	 * Retrieve graph factory instance
	 */
	private GraphFactory gf = GraphFactory.getInstance();
	
	/** The (directed) graph on which the variables are passed along. */
	private Graph<ILiteral, Set<IVariable>> sipGraph = 
		gf.createGraph(directed);

	/**
	 * Creates a SIP for the given rule with bindings for the given query.<b>
	 * NOTE: at the moment only the first literal of the head and the query are
	 * recognized.</b>
	 * 
	 * @param rule the rule for which to construct the graph
	 * @param query the query for this rule
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 * @throws IllegalArgumentException if the query is <code>null</code>
	 */
	public LeftToRightSip(final IRule rule, final IQuery query) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}
		if (query == null) {
			throw new IllegalArgumentException("The query must not be null");
		}

		constructSip(rule, getBoundsFromRuleHead(rule, query));
	}

	/**
	 * Constructs a sip out of a rule.
	 * @param rule the rule for which to create the sip
	 * @throws IllegalArgumentException if the rule is <code>null</code>
	 */
	public LeftToRightSip(final IRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("The rule must not be null");
		}

		final Set<IVariable> known = new HashSet<IVariable>();
		for (final ILiteral literal : rule.getHead()) {
			if (literal.getAtom().getPredicate() instanceof AdornedPredicate) {
				final Adornment[] adornments = ((AdornedPredicate) literal.getAtom().getPredicate()).getAdornment();
				int i = 0;
				for (final ITerm term : literal.getAtom().getTuple()) {
					if (adornments[i++] == Adornment.BOUND) {
						known.addAll(getVariables(term));
					}
				}
			}
		}

		constructSip(rule, known);
	}

	/**
	 * Determines the bound variables of a rule head according to the ground
	 * terms of a query.
	 * @param rule the rule for which to determine the bound head variables
	 * @param query the query for the rule
	 * @return the variables of the head bound by the query
	 */
	private static Set<IVariable> getBoundsFromRuleHead(final IRule rule, final IQuery query) {
		assert rule != null: "The rule must not be null";
		assert query != null: "The query must not be null";

		final Set<IVariable> bounds = new HashSet<IVariable>();

		// step through the query literals
		for (final ILiteral queryLiteral : query.getLiterals()) {
			final IPredicate queryPredicate = queryLiteral.getAtom().getPredicate();
			// step through the head literals
			for (final ILiteral headLiteral : rule.getHead()) {
				// extract all variables of the head, where the
				// query got ground terms if the predicates of
				// the head and query literal match
				if (headLiteral.getAtom().getPredicate().equals(queryPredicate)) {
					final ITuple headTuple = headLiteral.getAtom().getTuple();
					final ITuple queryTuple = queryLiteral.getAtom().getTuple();
					for (int i = 0, arity = headTuple.size(); i < arity; i++) {
						if (queryTuple.get(i).isGround()) {
							bounds.addAll(getVariables(headTuple.get(i)));
						}
					}
				}
			}
		}

		return bounds;
	}

	/**
	 * Returns the variables of a term. If the term is <code>null</code> or
	 * ground a empty set will be returned, if it is a constructed term all
	 * of its variables are returned, otherwise (in this case it must be a 
	 * variable) the term itself in a set will be returned.
	 * @param term the term for which to return the variables
	 * @return the set of variables in the term
	 */
	private static Set<IVariable> getVariables(final ITerm term) {
		if ((term == null) || term.isGround()) {
			return Collections.<IVariable>emptySet();
		} else if (term instanceof IConstructedTerm) {
			return ((IConstructedTerm) term).getVariables();
		}
		return Collections.singleton((IVariable) term);
	}

	/**
	 * Constructs the sip for a given rule and a known set of variables.
	 * @param rule the rule
	 * @param assumedKnown the known variables
	 */
	private void constructSip(final IRule rule, final Set<IVariable> assumedKnown) {
		assert rule != null: "The rule must not be null";
		assert assumedKnown != null: "The known collection must not be null";

		// map containing the variable->passedFromLiterals mappings
		final Map<IVariable, Set<ILiteral>> passings = new HashMap<IVariable, Set<ILiteral>>();

		// add the passings from head
		for (final ILiteral headLiteral : rule.getHead()) {
			final Set<IVariable> knownHeadVariables = headLiteral.getAtom().getTuple().getVariables();
			knownHeadVariables.retainAll(assumedKnown);
			for (final IVariable headVariable : knownHeadVariables) {
				Set<ILiteral> passingLiterals = passings.get(headVariable);
				if (passingLiterals == null) {
					passingLiterals = new HashSet<ILiteral>();
					passings.put(headVariable, passingLiterals);
				}
				passingLiterals.add(headLiteral);
			}
		}

		// iterating over the body literals and check the passings for
		// the variables of the literals
		for (final ILiteral literal : rule.getBody()) {
			// process the received bindings
			for (final IVariable variable : reveivedBindigs(literal, passings.keySet())) {
				final Set<ILiteral> passedFrom = passings.get(variable);
				if ((passedFrom != null) && !passedFrom.isEmpty()) { // there are some passings
					for (final ILiteral passingLiteral : passedFrom) {
						addEdge(passingLiteral, literal, Collections.singleton(variable));
					}
				}
			}
			// process the produced bindings
			for (final IVariable variable : producedBindings(literal, passings.keySet())) {
				Set<ILiteral> passedFrom = passings.get(variable);
				if (passedFrom == null) {
					passedFrom = new HashSet<ILiteral>();
					passings.put(variable, passedFrom);
				}
				passedFrom.add(literal);
			}
		}
	}

	/**
	 * Determines all variables, for which gained some bindings from previous literals.
	 * @param literal the literal receiving the passings.
	 * @param bound the bound variables until now
	 * @return the set of variables for which we got some passings
	 */
	private Set<IVariable> reveivedBindigs(final ILiteral literal, final Collection<IVariable> bound) {
		assert literal != null: "The literal must not be null";

		final Set<IVariable> passedVariables = new HashSet<IVariable>(literal.getAtom().getTuple().getVariables());
		passedVariables.retainAll(bound);

		return passedVariables;
	}

	/**
	 * Determines the set of variables for which the literal produces
	 * bindings.
	 * @param literal the literals which produces the passings
	 * @param bound the bound variables until now
	 * @return the variables for which passings are generated
	 */
	private Set<IVariable> producedBindings(final ILiteral literal, final Collection<IVariable> bound) {
		assert literal != null: "The literal must not be null";
		assert bound != null: "The bound variables must not be null";

		// negative literals don't produce any passings
		if (!literal.isPositive()) {
			return Collections.<IVariable>emptySet();
		}

		final IAtom atom = literal.getAtom();
		final Set<IVariable> variables = atom.getTuple().getVariables();

		// builtins, which can't calculate the missing variables don't
		// produce passings
		if (atom instanceof IBuiltinAtom) {
			final IBuiltinAtom builtin = (IBuiltinAtom) atom;
			if (!checkEvaluableBuiltin(builtin, bound)) {
				return Collections.<IVariable>emptySet();
			}
		}

		return variables;
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

		Set<Edge<ILiteral,Set<IVariable>>> edges = sipGraph.getEdges(source, target);
		
		// If there exists an edge (or edges) update the weights
		if (edges.size() > 0) {
			for (Edge<ILiteral, Set<IVariable>> edge : edges) {
					// Update the edge
					edge.getWeight().addAll(passedTo);
			}
		} else {
			// adding a new edge with the passings
			// adding the the literals as vertices
			sipGraph.add(source);
			sipGraph.add(target);
			
			Set<IVariable> weight = new HashSet<IVariable>(passedTo);
			
			// Create a new edge and add it to the sip graph
			sipGraph.add(gf.createEdge(source, target, weight));
		}
	}

	public Set<IVariable> getBoundVariables(final ILiteral literal) {
		if (literal == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		if (!sipGraph.getVertices().contains(literal)) {
			return Collections.<IVariable>emptySet();
		}

		final Set<IVariable> variables = new HashSet<IVariable>();

		for (final ILiteral predicate : Graphs.sort(sipGraph, literal, Direction.BACKWARD)) {
			// Add all labels from all edges to the variables set
			for (Edge<ILiteral, Set<IVariable>> edge : sipGraph.getEdges(predicate, literal)) {
				variables.addAll(edge.getWeight());
			}
		}
		return variables;
	}

	public Set<ILiteral> getDepends(final ILiteral literal) {
		final Set<ILiteral> dependencies = new HashSet<ILiteral>();
		final Set<ILiteral> todoDependencies = new HashSet<ILiteral>();
		if (literal == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		if (!sipGraph.getVertices().contains(literal)) {
			return Collections.<ILiteral>emptySet();
		}

		todoDependencies.add(literal);
		while (!todoDependencies.isEmpty()) {
			final ILiteral actual = todoDependencies.iterator().next();
			todoDependencies.remove(actual);

			for (final ILiteral vertex : Graphs.sort(sipGraph, actual, Direction.BACKWARD)) {
				if (dependencies.add(vertex)) {
					todoDependencies.add(vertex);
				}
			}
		}

		return dependencies;
	}

	public Set<Edge<ILiteral, Set<IVariable>>> getEdgesEnteringLiteral(
			final ILiteral literal) {
		if (literal == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		if (!sipGraph.getVertices().contains(literal)) {
			return Collections.<Edge<ILiteral, Set<IVariable>>>emptySet();
		}

		final List<ILiteral> predecessors = Graphs.sort(sipGraph, literal, Direction.BACKWARD);
		final Set<Edge<ILiteral, Set<IVariable>>> edges = 
			new HashSet<Edge<ILiteral, Set<IVariable>>>(predecessors.size());
		for (final ILiteral predecessor : predecessors) {
			// Add all edges to the edges set
			edges.addAll(sipGraph.getEdges(predecessor, literal));
		}
		return edges;
	}

	public Set<Edge<ILiteral, Set<IVariable>>> getEdgesLeavingLiteral(
			final ILiteral literal) {
		if (literal == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}

		if (!sipGraph.getVertices().contains(literal)) {
			return Collections.<Edge<ILiteral, Set<IVariable>>>emptySet();
		}

		final List<ILiteral> successors = Graphs.sort(sipGraph, literal);
		final Set<Edge<ILiteral, Set<IVariable>>> edges = 
			new HashSet<Edge<ILiteral, Set<IVariable>>>(successors.size());
		for (final ILiteral successor : successors) {
			edges.addAll(sipGraph.getEdges(literal, successor));
		}
		return edges;
	}

	public Set<IVariable> variablesPassedByLiteral(final ILiteral source,
			final ILiteral target) {
		if ((source == null) || (target == null)) {
			throw new IllegalArgumentException(
					"The source and the target must not be null");
		}

		if (!sipGraph.getVertices().contains(source) || !sipGraph.getVertices().contains(target)) {
			return Collections.<IVariable>emptySet();
		}

		final Set<IVariable> variables = new HashSet<IVariable>(getBoundVariables(source));
		
		// Get all variables (weights)
		for (Edge<ILiteral, Set<IVariable>> edge : sipGraph.getEdges(source, target)) {
			variables.addAll(edge.getWeight());
		}
		return variables;
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
		for (Edge<ILiteral, Set<IVariable>> edge : sipGraph.getEdges()) {
			buffer.append(edge).append(NEWLINE);
		}
		return buffer.toString();
	}

	public boolean containsVertex(final ILiteral literal) {
		if (literal == null) {
			throw new IllegalArgumentException("The literal must not be null");
		}
		return sipGraph.getVertices().contains(literal);
	}

	public Set<ILiteral> getRootVertices() {
		final Set<ILiteral> roots = new HashSet<ILiteral>();
		for (final ILiteral vertex : sipGraph.getVertices()) {
			if (Graphs.sort(sipGraph, vertex, Direction.BACKWARD).isEmpty()) {
				roots.add(vertex);
			}
		}
		return roots;
	}

	public Set<ILiteral> getLeafVertices() {
		final Set<ILiteral> leaves = new HashSet<ILiteral>();
		for (final ILiteral vertex : sipGraph.getVertices()) {
			if (Graphs.sort(sipGraph, vertex).isEmpty()) {
				leaves.add(vertex);
			}
		}
		return leaves;
	}

	public Comparator<ILiteral> getLiteralComparator() {
		return LITERAL_COMPARATOR;
	}

	/**
	 * Returns an unmodifiable set of all edges of the sip.
	 * 
	 * @return the set of edges.
	 */
	public Set<Edge<ILiteral, Set<IVariable>>> getEdges() {
		return Collections.unmodifiableSet(sipGraph.getEdges());
	}

	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof LeftToRightSip)) {
			return false;
		}
		final LeftToRightSip sip = (LeftToRightSip) object;
		return sipGraph.getEdges().equals(sip.sipGraph.getEdges());
	}

	public int hashCode() {
		int res = 17;
		res = res * 37 + sipGraph.getEdges().hashCode();
		return res;
	}

	/**
	 * Checks whether a built-in is evaluable.
	 * @param builtin the built-in atom
	 * @return <code>true</code> if the built-in is evaluable, otherwise
	 * <code>false</code>
	 */
	private static boolean checkEvaluableBuiltin(final IBuiltinAtom builtin, final Collection<IVariable> bound) {
		assert builtin != null: "The builtin atom must not be null";
		assert bound != null: "The bound variables must not be null";

		final Set<IVariable> unboundVariables = builtin.getTuple().getVariables();
		unboundVariables.removeAll(bound);
		return unboundVariables.size() <= builtin.maxUnknownVariables();
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

			if (!sipGraph.getVertices().contains(o1) && !sipGraph.getVertices().contains(o2)) { // none of the literals is in the graph
				return 0;
			} else if (!sipGraph.getVertices().contains(o1)) { // only o2 is in the graph
				return 1;
			} else if (!sipGraph.getVertices().contains(o2)) { // only o1 is in the graph
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
}
