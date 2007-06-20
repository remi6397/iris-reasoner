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
package org.deri.iris.api.evaluation.common;

import org.deri.iris.api.basics.IPredicate;

/**
 * <p>
 * Predicate with adornments to denote which of it's arguments are bound, and
 * which not.
 * </p>
 * <p>
 * $Id: IAdornedPredicate.java,v 1.1 2006-09-18 07:51:17 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-18 07:51:17 $
 */
public interface IAdornedPredicate extends IPredicate, IAdornedElement {

	/**
	 * Determines whether a predicate has the same sinature as this predicate.
	 * Same signature means same arity and predicate symbol.
	 * 
	 * @param pred
	 *            the other predicate to compare to
	 * @return true if they got the same signature
	 * @throws NullPointerException
	 *             if pred is null
	 */
	public abstract boolean hasSameSignature(final IPredicate pred);

	/**
	 * Returns this as an undadorned predicate.
	 * 
	 * @return the undadorned predicate
	 */
	public abstract IPredicate getUnadornedPredicate();

}