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
 * Interface or class description
 *
 * The Projection operation is meant to be used for projection on a 
 * relation (tree).
 *
 * Interface of a project operation used to promote modularity of 
 * inference engines.
 * 
 * Note: Implementation of the IProjection interface assumes an 
 * implementation of this interface itself and an implementation of 
 * the IRelationFactory interface (createProjectionOperator method-s).
 *  
 * @author Darko Anicic, DERI Innsbruck
 * @date   23.06.2006 17:19:58
 */

public interface IProjection {

	public IRelation project();
}
