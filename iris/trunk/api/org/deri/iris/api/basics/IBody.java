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
 * A body (a rule body) is a condition part of a rule. For a rule
 * </p>
 * <p>
 * q :- p1, p2,...,pn.
 * </p>
 * <p>
 * the body of a rule consist of literals p1, p2,...,pn.
 * </p>
 * <p>
 * $Id: IBody.java,v 1.8 2007-10-14 14:48:59 bazbishop237 Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 17:21:21
 */
public interface IBody {

	/**
	 * <p>
	 * Returns the length of the body (the number of literals contained in the
	 * body).
	 * </p>
	 * 
	 * @return The length of the body.
	 */
	public int getLength();

	/**
	 * <p>
	 * Returns the literal at the specified position in this list.
	 * </p>
	 * 
	 * @param arg
	 *            Index of element to return.
	 * @return The literal at the specified position in this list.
	 */
	public ILiteral getLiteral(int arg);

	/**
	 * <p>
	 * Returns a list of all literals of the body.
	 * </p>
	 * 
	 * @return A list of all literals.
	 */
	public List<ILiteral> getLiterals();

	/**
	 * <p>
	 * Returns a list of all variables of the body.
	 * </p>
	 * 
	 * @return A list of all variables.
	 */
	public List<IVariable> getVariables();
}
