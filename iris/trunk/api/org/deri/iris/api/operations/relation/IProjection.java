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

import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * An interface for the projection operation.
 * The projection operation is performed on a relation R. The 
 * operation is used to remove some of the components 
 * (attributes) and/to rearrange some of the remaining components of R.
 * </p>
 * <p>
 * @see org.deri.iris.api.factory.IRelationOperationsFactory#
 * </p>
 *  
 * @author Darko Anicic, DERI Innsbruck
 * @date   23.06.2006 17:19:58
 */

public interface IProjection {

	/**
	 * <p>
	 * Performs the projection operation.
	 * </p>
	 * 
	 * @return	The relation which is a result of the projection operation.
	 */
	public IMixedDatatypeRelation project();
}
