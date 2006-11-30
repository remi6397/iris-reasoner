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

package org.deri.iris.api.evaluation;

import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.storage.IRelation;

/**
 * Represents the result set of a query.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   09.11.2006 09:44:49
 */
public interface IResultSet {
	
	/**
	 * Get the result as Map<IPredicate, IRelation>.
	 * @return A map of predicates and relations (for each predicate, 
	 * 		   there is a relation with tuples, as result, assigned 
	 * 		   to it)
	 */
	public Map<IPredicate, IRelation> getResults();
}
