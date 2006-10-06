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
import org.deri.iris.basics.MinimalTuple;

/**
 * Implementation of the Comparator interface meant to be 
 * used for creating an index tree. 
 * This implementation handles duplicate tuples. Duplicate tuples
 * are those tuples that have identical terms on positions defined by sort
 * indexes. 
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 14:20:06
 */
public class IndexComparator extends BasicComparator{

	public IndexComparator(final int[] sortIndexes) {
		super(sortIndexes);
	}

	/* 
	 * Implementation of the Comparator interface meant to be 
	 * used for creating an index tree. This tree is sorted on some 
	 * particular index (or set of indexes) where duplicate tuples are 
	 * grouped in one single tree node. Duplicate tuples are those tuples 
	 * that have identical terms on positions defined by sort indexes. 
	 * 
	 * Also see:
	 * (non-Javadoc)
	 * @see org.deri.iris.operations.tuple.BasicComparator#compare(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple)
	 */
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
		int comparison = 0;
		int forEachRelevantIndex = 0;
		int forEachIndex = 0;
		int equal = 0;
		for(int i=0; i<this.getSortIndexes().length; i++){
			if(t0.getArity()>i && t1.getArity()>i){
			comparison = t0.getTerm(i).compareTo(t1.getTerm(i));
			if(comparison == 0) forEachIndex++;
			if(this.getSortIndexes()[i] != -1){
				comparison = t0.getTerm(i).compareTo(
						t1.getTerm(this.getSortIndexes()[i]));
		    	forEachRelevantIndex++;
				if(comparison != 0){
					return comparison;
				}else{
					equal++;
				}
			}
			}else 
				break;
		}
		if(compareAsc()){
			for(int i=0; i<this.getSortIndexes().length; i++){
				comparison = t0.getTerm(i).compareTo(t1.getTerm(i));
				if(comparison != 0){
					return comparison;
				}
			}
			return 0;
		}else{
			/* 
			 * Tuples t0 and t1 are duplicates if they have identical terms  
			 * for each sort index.
			 */
			if(forEachRelevantIndex == equal && !(t0 instanceof MinimalTuple) &&
					!(t1 instanceof MinimalTuple)){
				ITuple tmp = t1.getDuplicate();
				t0.setDuplicate(tmp);
				t1.setDuplicate(t0);
			}
			return 0;
		}
	}
	
	/**
	 * @return true if comparing shoud be in ascending or
	 */
	private boolean compareAsc(){
		for(int i=0; i<this.getSortIndexes().length; i++)
			if(this.getSortIndexes()[i] != -1) return false;
		
		return true;
	}
}
