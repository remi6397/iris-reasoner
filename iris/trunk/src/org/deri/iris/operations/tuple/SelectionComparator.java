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
 * This implementation does not defines a compare method that is used
 * for comparing two tuples in the selection operation.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 11:08:41
 */
public class SelectionComparator extends BasicComparator{

	public SelectionComparator(int arity) {
		super(arity);
		// TODO Auto-generated constructor stub
	}
	
	public SelectionComparator(final int[] sortIndexes) {
		super(sortIndexes);
	}

	// Correct it!
	public int compare(ITuple t0, ITuple t1){
		if (t0.getArity() != t1.getArity()) {
			throw new IllegalArgumentException("Couldn't compare due to different arity of tuples.");
		} 
		int comparison = 0;
		int equal = 10;
		//if(this.sortIndexes == null) this.sortIndexes = getDefaultSortIndexes(t0.getArity());
		for(int i=0; i<this.getSortIndexes().length; i++){
			/* coompare tuples on each index that is differnt from -1.
			 * sortIndexes[i] == -1 means the term with that index
			 * is not relevant for the current sorting.
			 */
		    if(this.getSortIndexes()[i] != -1){
		    	comparison = t0.getTerm(i).compareTo(
						t1.getTerm(this.getSortIndexes()[i]));
				//if(comparison != 0){
		    	if(comparison > 0){
					return comparison;
				}
		    	if(comparison == 0){
					return 0;
				}
		    	/*else{
					return 0;
				}*/
			}
		}
		return comparison;
	}
}
