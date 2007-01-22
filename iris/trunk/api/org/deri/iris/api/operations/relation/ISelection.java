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
 *<p>
 * An interface for the selection operation.
 * A selection is a unary operation written as SELECT(a theta b)(R) 
 * or SELECT(a theta v)(R) where:
 * <ul>
 * <li> a and b are attribute names</li>
 * <li> theta is a binary operation in the set (relation)</li>
 * <li> v is a value constant</li>
 * <li> R is a relation</li>
 * </ul>
 * The selection SELECT(a theta b)(R) selects all those tuples in R for 
 * which theta holds between the a and the b attribute.
 * </p>
 * <p>
 * @see org.deri.iris.api.factory.IRelationOperationsFactory#
 * </p>
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:18:20
 *
 * @version $Revision: 1.8 $ $Date: 2007-01-22 12:46:04 $
 */
public interface ISelection {
	
	/**
	 * Performs the selection operation.
	 * 
	 * @return	The relation which is a result of the selection operation.
	 */
	public IRelation select();
}
