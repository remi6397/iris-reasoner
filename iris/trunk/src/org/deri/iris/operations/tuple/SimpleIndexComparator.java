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

import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

public class SimpleIndexComparator extends BasicComparator{

	public SimpleIndexComparator(final int[] sortIndexes) {
		super(sortIndexes);
	}

	public int compare(ITuple t0, ITuple t1){
        final int t0Arity = t0.getArity();
        final int t1Arity = t1.getArity();
        
		if (t0Arity != t1Arity) {
			throw new IllegalArgumentException(
					"Couldn't compare due to different arity of tuples.");
		}
        
        final int[] idx = getSortIndexes();        
		if (t0Arity != idx.length) {
			throw new IllegalArgumentException(
					"The length of sort indexe array does not match " +
					"the arity of the compared tuples.");
		}
		int comparison = 0;

		// copying some frequently needed values, to save some cpu time
		final List<ITerm> t0t = t0.getTerms();
		final List<ITerm> t1t = t1.getTerms();

		for(int i=0; i < idx.length; i++){
			if(idx[i] != -1){
				comparison = t0t.get(i).compareTo(t1t.get(idx[i]));
				if(comparison != 0) {
					return comparison;
				}
			}
		}
		for(int i=0; i < idx.length; i++){
			if(idx[i] == -1){
				comparison = t0t.get(i).compareTo(t1t.get(i));
				if(comparison != 0) {
					return comparison;
				}
			}
		}
		return 0;
	}
}
