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
package org.deri.iris.optimisations;

import java.util.Comparator;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.graph.LabeledEdge;

/**
 * <p>
 * A SIP (Sideway Information Passing) to determine which variable of which
 * literal was passed/bound by which literal.
 * </p>
 * <p>
 * $Id: ISip.java,v 1.4 2007-10-25 07:18:49 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.4 $
 */
public interface ISip {

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
	public abstract Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdgesEnteringLiteral(
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
	public abstract Set<LabeledEdge<ILiteral, Set<IVariable>>> getEdgesLeavingLiteral(
			final ILiteral l);

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
