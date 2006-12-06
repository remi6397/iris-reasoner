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

/**
 * <p>
 * A literal (a subgoal) is either an atomic formula or a negated atomic
 * formula:
 * </p>
 * <p>
 * p(A1,...An) or not p(A1,...An)
 * </p>
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: ILiteral.java,v 1.3 2006-12-06 16:35:45 darko Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 17:20:27
 */
public interface ILiteral<Type extends ILiteral> extends IAtom<Type> {

	/**
	 * <p>
	 * Checks whether the literal is a positive atomic formula or a negated
	 * atomic formula.
	 * </p>
	 * 
	 * @return True if the literal is a positive atomic formula; false
	 *         otherwise.
	 */
	public boolean isPositive();

	/**
	 * <p>
	 * Sets the literal to be either a positive atomic formula or a negated
	 * atomic formula.
	 * </p>
	 * 
	 * @param arg
	 *            If true, the literal is a positive atomic formula; if false,
	 *            the literal is a negated atomic formula.
	 */
	public void setPositive(boolean arg);

	/**
	 * <p>
	 * Returns the atom of this literal.
	 * <p/>
	 * 
	 * @return The atom.
	 */
	public IAtom getAtom();
}
