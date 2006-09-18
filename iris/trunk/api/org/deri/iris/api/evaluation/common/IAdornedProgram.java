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

import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;

/**
 * <p>
 * Defines a Adorned Programm and some methods to get various information about
 * it.
 * </p>
 * <p>
 * $Id: IAdornedProgram.java,v 1.1 2006-09-18 07:51:16 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-18 07:51:16 $
 */
public interface IAdornedProgram {

	/**
	 * Return a set of all adorned rules.
	 * 
	 * @return the set of adorned rules
	 */
	public abstract Set<IAdornedRule> getAdornedRules();

	/**
	 * Returns the original rules for which the adorned rules where created
	 * 
	 * @return
	 */
	public abstract Set<IRule> getNormalRules();

	/**
	 * Returns a set of all adorned predicates of this program.
	 * 
	 * @return the set of adorned predicates
	 */
	public abstract Set<IAdornedPredicate> getAdornedPredicates();

	/**
	 * Returns all predicates which might be derived.
	 * 
	 * @return the derived predicates
	 */
	public abstract Set<IPredicate> getDerivedPredicates();

	/**
	 * Returns the query for this program.
	 * 
	 * @return the query
	 */
	public abstract IQuery getQuery();

}