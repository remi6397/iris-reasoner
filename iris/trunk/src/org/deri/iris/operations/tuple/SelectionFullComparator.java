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

import org.deri.iris.api.basics.ITuple;

/**
 * Implementation of the Comparator interface meant to be 
 * used for creating an index tree. This tree is sorted based on some 
 * particular indexe/s. For instance if indezes are <1, 2, 1, 2> we want
 * to group those tuples which have first and third term equal and second 
 * and fourth term equal. Thus for these indexes if:
 * 
 * t0 = <a, b, a, b>
 * t1 = <a, a, c, d>
 * 
 * after compareing t0 and t1, it will be returned 0 and t1 will not be
 * stored in the tree at all, as it does not satisfies the condition (
 * first and third term equal and second 
 * and fourth term equal). While for tuples:
 * 
 * t0 = <a, b, a, b>
 * t1 = <c, a, c, a>
 * 
 * wehere both tuplese satisfy the condition, the comparing will be performed
 * in ascending order using compareTo method (and both tuples will be stored 
 * in the tree).
 * 
 * SelectionFullComparator handls also tuples which may have null values for 
 * their terms, e.g.
 * 
 * t0 = <a, null, b, null>
 * 
 * Also see:
 * (non-Javadoc)
 * @see org.deri.iris.operations.tuple.BasicComparator#compare(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple)
 *
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 14:20:06
 */
public class SelectionFullComparator extends SelectionComparator{

	public SelectionFullComparator(final int[] sortIndexes) {
		super(sortIndexes);
	}

	@SuppressWarnings("unchecked")
	public int compare(ITuple t0, ITuple t1){
		if (t0.getArity() != t1.getArity()) {
			throw new IllegalArgumentException(
					"Couldn't compare due to different arity of tuples.");
		} 
		int quota = getQuota();
		int comp = 0;
		
		if(quota != checkTerms(t0)) return 0;
		
		if(quota != checkTerms(t1)) return 0;
		
		for(int i=0; i<this.getSortIndexes().length; i++){
			if(this.getSortIndexes()[i] != 0){
				for(int j=0; j<this.getSortIndexes().length; j++){
					if(this.getSortIndexes()[i] == this.getSortIndexes()[j])
						if(this.getSortIndexes()[j] > 0){
							if(! t0.getTerm(i).getValue().equals(t1.getTerm(j).getValue())) 	
								return 0;
						}else{
							if(t0.getTerm(i).getValue().equals(t1.getTerm(j).getValue())) 	
								return 0;
						}
				}
			}
		}
		
		for(int i=0; i<this.getSortIndexes().length; i++){
			if(t0.getTerm(i) == null){
				if(t1.getTerm(i) != null) return -1;
			}else{
				if(t0.getTerm(i).getValue() == null &&
						t1.getTerm(i).getValue() != null)
						return -1;
			}
			
			if(t1.getTerm(i) == null){
				if(t0.getTerm(i) != null) return 1;
			}else{
				if(t1.getTerm(i).getValue() == null &&
						t0.getTerm(i).getValue() != null)
						return 1;
			}
		
			if(t0.getTerm(i) != null &&
					t1.getTerm(i) != null)
			if(t0.getTerm(i).getValue() != null &&
					t1.getTerm(i).getValue() != null){
				
				comp = t1.getTerm(i).compareTo(t0.getTerm(i));
			}
			if(comp != 0) return comp; 
		}
		return 0;
	}
}

