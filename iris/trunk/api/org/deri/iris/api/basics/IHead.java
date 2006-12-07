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
 * dation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.iris.api.basics;

import java.util.List;

import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * A head (a rule head) is a conclusion part of a rule. For a rule:
 * </p>
 * <p>
 * q :- p1, p2,...,pn
 * </p>
 * <p>
 * the head of a rule consist of a single positive literal q.
 * </p>
 * <p>
 * NOTE: Currently, rules with only one literal in the head are supported.
 * </p>
 * <p>
 * $Id: IHead.java,v 1.6 2006-12-07 16:37:08 darko Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 17:21:21
 */
public interface IHead {

	/**
	 * <p>
	 * Returns the length of the head (the number of literals contained in the
	 * body).
	 * </p>
	 * 
	 * @return The length of the head.
	 */
	public int getHeadLenght();

	/**
	 * <p>
	 * Returns the literal at the specified position in this list.
	 * </p>
	 * 
	 * @param arg
	 *            Index of element to return.
	 * @return The literal at the specified position in this list.
	 */
	public ILiteral getHeadLiteral(int arg);

	/**
	 * <p>
	 * Returns a list of all literals of the head.
	 * </p>
	 * 
	 * @return A list of all literals.
	 */
	public List<ILiteral> getHeadLiterals();

	/**
	 * <p>
	 * Returns a list of all variables of the head.
	 * </p>
	 * 
	 * @return A list of all variables.
	 */
	public List<IVariable> getHeadVariables();
}
