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
package org.deri.iris.operations.relations;

import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.storage.IRelation;

/**
 * USE Join with projectIndexes which can handle projection operation too!
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   30.05.2006 10:44:38
 */
public class Projection implements IProjection{
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.operations.relation.IProjection#project(org.deri.iris.api.storage.IRelation, int[])
	 */
	public IRelation project(IRelation relation, int[] indexes) {
		
		return null;
	}
	
	/*private ITuple projectTuple(ITuple t){
		if(projectIndexes == null)return concatenate(arg0, arg1);
		List tupleList = new LinkedList();
		for(int i=0; i<projectIndexes.length; i++){
			if(projectIndexes[i] != -1){
				if(i < arg0.getArity())
					tupleList.add(arg0.getTerm(i));
				else
					tupleList.add(arg1.getTerm(i-arg0.getArity()));
			}
		}
		return Factory.BASIC.createTuple(tupleList);
	}*/
}
