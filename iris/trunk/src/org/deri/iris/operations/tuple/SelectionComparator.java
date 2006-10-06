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
 * particular indexe/s. For instance if indexes are <1, 2, 1, 2> we want
 * to group those tuples which have first and third term equal and second 
 * and fourth term equal. Thus for these indexes if:
 * 
 * t0 = <a, b, a, b>
 * t1 = <a, a, c, d>
 * 
 * after compareing t0 and t1, it will be returned 0. This (returned value = 0)
 * causes that t1 will not be stored in the tree at all, as it does not 
 * satisfies the condition (first and third term equal and second 
 * and fourth term equal). While for tuples:
 * 
 * t0 = <a, b, a, b>
 * t1 = <c, a, c, a>
 * 
 * wehere both tuplese satisfy the condition, the comparing will be performed
 * in ascending order using compareTo method (and both tuples will be stored 
 * in the tree).
 * 
 * Also see:
 * (non-Javadoc)
 * @see org.deri.iris.operations.tuple.BasicComparator#compare(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple)
 *
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 14:20:06
 */
public class SelectionComparator extends BasicComparator{

	public SelectionComparator(final int[] sortIndexes) {
		super(sortIndexes);
	}
	
	public int compare(ITuple t0, ITuple t1){
		if (t0.getArity() != t1.getArity()) {
			throw new IllegalArgumentException(
					"Couldn't compare due to different arity of tuples.");
		} 
		if (t0.getArity() != this.getSortIndexes().length) {
			throw new IllegalArgumentException(
					"The length of sort indexe array does not match " +
					"the arity of the compared tuples.");
		}
		int quota = getQuota();
		
		if(quota != checkTerms(t0)) return 0;
		
		if(quota != checkTerms(t1)) return 0;
		
		return checkTuples(t0, t1);
	}
	
	public int getQuota(){
		int quota = 0;
		for(int i=0; i<this.getSortIndexes().length; i++){
			if(this.getSortIndexes()[i] != 0){
				for(int j=i+1; j<this.getSortIndexes().length; j++){
					if(this.getSortIndexes()[i] == this.getSortIndexes()[j])
						quota++;
				}
			}
		}
		return quota;
	}
	
	/**
	 * For:
	 * 
	 * indexes = <1, 2, 1, 2, -1, -1>
	 * 
	 * checks wheter it is, for instance, a case:
	 * 
	 * t = <a, b, a, b, c, d>
	 * 
	 * (1st and 3rd terms are the same ones as well as
	 * 2nd and 4th ones, while 5th and 6th are different). 
	 * 
	 * @param t tuple which terms need to be compared
	 * @return number of terms that are equal
	 */
	public int checkTerms(ITuple t){
		int comp0 = 0;
		for(int i=0; i<this.getSortIndexes().length; i++){
			if(this.getSortIndexes()[i] != 0){
				for(int j=i+1; j<this.getSortIndexes().length; j++){
					if(this.getSortIndexes()[i] == this.getSortIndexes()[j])
						if(this.getSortIndexes()[j] > 0){
							if(t.getTerm(i).getValue().equals(t.getTerm(j).getValue())) 
								comp0++;
						}else{
							if(! t.getTerm(i).getValue().equals(t.getTerm(j).getValue())) 
								comp0++;
						}
					
				}
			}
		}
		return comp0;
	}
	
	/**
	 * Ensures the ascending order in cases wehere both tuplese satisfy the 
	 * condition,(both tuples will be stored in the tree).
	 * 
	 * @param t0 tuple to be compared
	 * @param t1 tuple to be compared
	 * @return a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
	 */
	public int checkTuples(ITuple t0, ITuple t1){
		int comp0 = 0;
		for(int i=0; i<this.getSortIndexes().length; i++){
			comp0 = t1.getTerm(i).compareTo(t0.getTerm(i));
			if(comp0 != 0) 
				return comp0; 
		}
		return comp0;
	}
}

