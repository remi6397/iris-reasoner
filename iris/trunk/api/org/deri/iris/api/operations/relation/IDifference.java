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
package org.deri.iris.api.operations.relation;

import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * An interface for the set difference operation. The difference 
 * of relations R and S, denoted R-S, is the set of tuples in 
 * R but not in S. Relations R and S are supposed to have the 
 * same arity.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date  11.04.2006 @time  15:25:17
 *
 * @version $Revision: 1.4 $ $Date: 2007-01-22 12:46:04 $
 */
public interface IDifference {

	/**
	 * <p>
	 * Performs set difference operation.
	 * </p>
	 * <p>
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory#
	 * createDifferenceOperator(IRelation arg0, IRelation arg1).
	 * </p>
	 * 
	 * @return	The relation which is result of the set difference operation.
	 */
	public IRelation difference();
}
