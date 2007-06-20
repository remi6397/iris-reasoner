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

import java.util.Set;

import org.deri.iris.api.basics.IQuery;
import org.deri.iris.exception.DataModelException;

/**
 * <p>
 * This interface extends IEvaluator interface adding methods for 
 * querying. Namely in the bottom-up approach, first, the whole 
 * logic program (with  the entire knowledge base) is evaluated. 
 * The querying takes place once the whole possible intestinal 
 * data is computed. Thus explicit methods for querying, in the
 * bottom-up approach, enables a user to pose as many queries as
 * necessary, once the logic program is evaluated. 
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date Jan 25, 2007
 */
public interface IBottomUpEvaluator extends IEvaluator{

	/**
	 * Evaluates a query against a pre-computed program. 
	 * 
	 * @param q	A query to be answered.
	 * @return	True if the query evaluation has been successfully terminated,
	 *         	otherwise false.
	 * @throws 	DataModelException
	 */
	public boolean runQuery(IQuery q) 
		throws DataModelException;
	
	/**
	 * Evaluates a set of queries against a pre-computed program. 
	 * 
	 * @param queries	A set of queries to be answered.
	 * @return			True if the query evaluation has been successfully terminated,
	 *         			otherwise false.
	 * @throws 			DataModelException
	 */
	public boolean runQueries(Set<IQuery> queries) 
		throws DataModelException;
}
