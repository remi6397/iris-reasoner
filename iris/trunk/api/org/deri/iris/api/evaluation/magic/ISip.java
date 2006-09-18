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
package org.deri.iris.api.evaluation.magic;

import java.util.Comparator;
import java.util.Set;

import org._3pq.jgrapht.Edge;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.magic.SipHelper;
import org.deri.iris.graph.LabeledDirectedEdge;

/**
 * @author richi
 * 
 */
public interface ISip {

	/**
	 * <p>
	 * Updates the sip. If it doesn't contains an edge from the source whitch
	 * passes the given variables to the target the specific edge will be added.
	 * If it contains such an edge, the variables in passedTo will be added to
	 * the passed variables.
	 * </p>
	 * <p>
	 * <b>ATTENTION: There's no error checking whether the sip is still valid.</b>
	 * </p>
	 * 
	 * @param source
	 *            the source literal
	 * @param target
	 *            the literal the variables are passed to
	 * @param passedTo
	 *            the variables whitch are passed
	 * @throws NullPointerException
	 *             if the source, target or set of variables is null
	 */
	public abstract void updateSip(final ILiteral source,
			final ILiteral target, final Set<IVariable> passedTo);

	/**
	 * Determines all variables, wich are passed to this literal.
	 * 
	 * @param l
	 *            the literal for which to determine the variables
	 * @return the set of variables
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public abstract Set<IVariable> getBoundVariables(final ILiteral l);

	/**
	 * Searches for literals on which the submitted literal depends.
	 * 
	 * @param l
	 *            the literal for which to search for dependencies
	 * @return the set of literal on which the submitted literal depends
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public abstract Set<ILiteral> getDepends(final ILiteral l);

	/**
	 * Searches for edges entering this literal.
	 * 
	 * @param l
	 *            the literal for which to search for entering edges
	 * @return set of edges entering this literal
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public abstract Set<LabeledDirectedEdge<Set<IVariable>>> getEdgesEnteringLiteral(
			final ILiteral l);

	/**
	 * Searches for edges leaving this literal.
	 * 
	 * @param l
	 *            the literal for which to search for entering edges
	 * @return set of edges entering this literal
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public abstract Set<LabeledDirectedEdge<Set<IVariable>>> getEdgesLeavingLiteral(
			final ILiteral l);

	/**
	 * Determines the set of variables passed to one literal by one specific
	 * edge.
	 * 
	 * @param e
	 *            edge which passes the variables
	 * @return the set of variables
	 * @throws NullPointerException
	 *             if the edge is null
	 * @throws IllegalArgumentException
	 *             if one of the vertices of the edge isn't a literal
	 */
	public abstract Set<IVariable> variablesPassedByEdge(final Edge e);

	/**
	 * Determines the set of variables passed to one literal by one specific
	 * edge.
	 * 
	 * @param source
	 *            the source of the edge
	 * @param target
	 *            the target of the edge
	 * @return the set of variables
	 * @throws NullPointerException
	 *             if one of the literal is null
	 */
	public abstract Set<IVariable> variablesPassedByLiteral(
			final ILiteral source, final ILiteral target);

	/**
	 * Returns the rule associated with this sip.
	 * 
	 * @return the rule
	 */
	public abstract IRule getRule();

	/**
	 * Returns the query associated with this sip.
	 * 
	 * @return the query
	 */
	public abstract IQuery getQuery();

	/**
	 * <p>
	 * Creates a copy of this sip.
	 * </p>
	 * <p>
	 * Nothing will be cloned except the graph.
	 * </p>
	 * 
	 * @return the copy
	 * @throws NullPointerException
	 *             if the rule of the query are null
	 */
	public abstract ISip defensifeCopy();

	/**
	 * <p>
	 * Creates a copy of this sip. There will be no check whether the rule
	 * matches to teh sip, or not, so inconsistency checks and repair work must
	 * be done by the user of this method.
	 * </p>
	 * <p>
	 * Nothing will be cloned except the graph.
	 * </p>
	 * 
	 * @param r
	 *            the new rule for the copy
	 * @param q
	 *            the query for the copy
	 * @return the copy
	 * @throws NullPointerException
	 *             if the rule of the query are null
	 */
	public abstract ISip defensifeCopy(final IRule r, final IQuery q);

	/**
	 * <p>
	 * Exchanges a literal in the sip. All the edges entering and leaving the
	 * original vertex will now enter of leave the new one.
	 * </p>
	 * <p>
	 * <b>ATTENTION: There's no error checking whether the sip is still valid.</b>
	 * </p>
	 * 
	 * @param from
	 *            the old literal
	 * @param to
	 *            the new literal
	 * @throws NullPointerException
	 *             if one of the literals is null
	 */
	public abstract void exchangeLiteral(final ILiteral from, final ILiteral to);

	/**
	 * <p>
	 * Removes a edge from the sip graph.
	 * </p>
	 * <p>
	 * <b>ATTENTION: There's no error checking whether the sip is still valid.</b>
	 * </p>
	 * 
	 * @param e
	 *            the edge to remove
	 * @throws NullPointerException
	 *             if the edge is null
	 */
	public abstract void removeEdge(final LabeledDirectedEdge<Set<IVariable>> e);

	/**
	 * <p>
	 * Removes the edge with the given source and target from the sip graph.
	 * </p>
	 * <p>
	 * <b>ATTENTION: There's no error checking whether the sip is still valid.</b>
	 * </p>
	 * 
	 * @param source
	 *            the source literal
	 * @param target
	 *            the target literal
	 * @throws NullPointerException
	 *             if one of the literals is null
	 */
	public abstract void removeEdge(final ILiteral source, final ILiteral target);

	/**
	 * Determines whether the sip constains a specific literal.
	 * 
	 * @param l
	 *            the literal for which to search for
	 * @return whether or not the literal is in the sip
	 * @throws NullPointerException
	 *             if the literal is null
	 */
	public abstract boolean containsVertex(final ILiteral l);

	/**
	 * Determines the roots of this graph. A root is a Literal (vertex) with no
	 * entering arcs.
	 * 
	 * @return the set of literals with no entering arcs
	 */
	public abstract Set<ILiteral> getRootVertices();

	/**
	 * Determines the leafes of this graph. A leafe is a Literal (vertex) with
	 * no outfgoing arcs.
	 * 
	 * @return the set of literals with no outfgoing arcs
	 */
	public abstract Set<ILiteral> getLeafVertices();

	/**
	 * Returns the Comparator to compare literals according to their position in
	 * the sips. If you want to use this comparator with adorned literals use
	 * the SipHelper.getAdornedSip(AdornedRule) method to get the sip for
	 * adorned literals, otherwise it wont work as expected.
	 * 
	 * @return the comparator to compare literals
	 * @see SipHelper#getAdornedSip(org.deri.iris.evaluation.common.AdornedProgram.IAdornedRule)
	 */
	public abstract Comparator<ILiteral> getLiteralComparator();

}