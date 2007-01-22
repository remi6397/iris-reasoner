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
 * An interface for the set intersection operation.
 * The intersection of two relations (sets) A and B is the relation
 * that contains all elements of A that also belong to B 
 * (or equivalently, all elements of B that also belong to A), 
 * but no other elements.
 * </p>
 * 
 * @author Darko Anicic
 * @date  11.04.2006 15:42:32
 *
 * @version $Revision: 1.3 $ $Date: 2007-01-22 12:46:04 $
 */
public interface IIntersection {

	public IRelation intersection(final IRelation arg0, final IRelation arg1);
}
