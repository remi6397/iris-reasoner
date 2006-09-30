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
package org.deri.iris.operations.tuple;

import static org.deri.iris.factory.Factory.BASIC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IConcatenation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 11:59:38
 */
public class Concatenation implements IConcatenation{
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.operations.tuple.IConcatenation#concatenate(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple)
	 */
	@SuppressWarnings({"unchecked","unchecked"})
	public ITuple concatenate(ITuple arg0, ITuple arg1) {
		List tupleList = new LinkedList();
		tupleList.addAll(arg0.getTerms());
		tupleList.addAll(arg1.getTerms());
		
		return Factory.BASIC.createTuple(tupleList);
	}

	/* 
	 * Projection
	 * 
	 * (non-Javadoc)
	 * @see org.deri.iris.api.operations.tuple.IConcatenation#concatenate(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple, int[])
	 */
	public ITuple concatenate(ITuple arg0, ITuple arg1, 
			int[] projectIndexes) {
		
		ITerm[] terms = new ITerm[projectIndexes.length];
		List<ITerm> termList = new ArrayList<ITerm>();
		ITuple t = concatenate(arg0, arg1);
		for(int i=0; i<projectIndexes.length; i++){
			if(projectIndexes[i] != -1)
				terms[projectIndexes[i]] = t.getTerm(i);
		}	
		for(int j=0; j<projectIndexes.length; j++){
			if(terms[j] != null)
				termList.add(terms[j]);
		}
		return BASIC.createTuple(termList);
	}
}
