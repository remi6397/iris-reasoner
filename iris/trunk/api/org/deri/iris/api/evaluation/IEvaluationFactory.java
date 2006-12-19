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
 * <p>
 * Interface of a factory for creating objects from the evaluation package.
 * </p>
 * <p>
 * $Id: IEvaluationFactory.java,v 1.2 2006-12-19 18:11:41 darko Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 09.11.2006 10:22:34
 */
public interface IEvaluationFactory {

	/**
	 * Creates an empty ResultSet ready 
	 * to be filled with results after a query evaluation.
	 * @return ResultSet
	 */
	public IResultSet createResultSet();
	/**
	 * Creates a ResultSet with results represented as a map.
	 * @param m Result
	 * @return  A ResultSet containig result m 
	 */
	public IResultSet createResultSet(Map<IPredicate,IRelation> m);
}
