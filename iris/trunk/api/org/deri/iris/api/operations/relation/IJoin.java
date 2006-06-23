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
import org.deri.iris.operations.relations.JoinCondition;

/**
 * Interface or class description
 *
 * Interface of a join operation used to promote modularity 
 * of inference engines.
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:09:02
 *
 * @version $Revision: 1.4 $ $Date: 2006-06-23 14:21:06 $
 * @param <JoinCondition>
 */
public interface IJoin {
	
	/**
	 * Default join or equijoin is a join where a condition is '='
	 * 
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @return
	 */
	public IRelation equiJoin(final IRelation arg0, final IRelation arg1, 
			int[] indexes);
	
	/**
	 * General join operation where a specified condition 
	 * (=, !=, <, >, <=, >=) must hold.
	 * 
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @return
	 */
	public IRelation join(IRelation arg0, IRelation arg1, 
			int[] indexes, JoinCondition condition);
		
}
