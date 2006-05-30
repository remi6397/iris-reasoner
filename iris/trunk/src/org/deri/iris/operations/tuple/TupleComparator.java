/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.iris.operations.tuple;

import java.util.Comparator;

import org.deri.iris.api.basics.ITuple;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 10:23:34
 */
public class TupleComparator implements Comparator<ITuple> {

	private int[] sortIndexes;

	/**
	 * Default comparator - ascending order
	 * 
	 * @param arity
	 * 				of tuples that will be compared
	 */
	public TupleComparator(int arity) {
		this.sortIndexes = this.getDefaultSortIndexes(arity);
	}

	/**
	 * Comparator defined by sort indexes
	 * 
	 * @param sortIndexes
	 */
	public TupleComparator(final int[] sortIndexes) {
		this.sortIndexes = sortIndexes;
	}

	/**
	 * Compares its two arguments for order. Comparison is based on 
	 * the sort indexes. Returns a negative integer, zero, or a positive
	 * integer as the first argument is less than, equal to, or greater 
	 * than the second. If two tuples are equal comparing them on 
	 * the sort indexes then compare them in an ascending order.
	 * This method is used whenever a new tuple is added to a tree or 
	 * an index tree.
	 * 
	 * @param t0
	 * 			the first tuple to be compared.
	 * @param t1
	 * 			the second tuple to be compared.
	 * @return
	 * 			a negative integer, zero, or a positive integer as 
	 * 			the first argument is less than, equal to, or greater 
	 * 			than the second.
	 * 
	 * @throws IllegalArgumentException
	 * 			- if the arguments' arity prevent them from being 
	 * 			compared by this Comparator.
	 */
	@SuppressWarnings("unchecked")
	public int compare(ITuple t0, ITuple t1){
		if (t0.getArity() != t1.getArity()) {
			throw new IllegalArgumentException("Couldn't compare due to different arity of tuples.");
		} 
		int comparison = 0;
		int equal = 10;
		if(this.sortIndexes == null) this.sortIndexes = getDefaultSortIndexes(t0.getArity());
		for(int i=0; i<this.sortIndexes.length; i++){
			/* coompare tuples on each index that is differnt from -1.
			 * sortIndexes[i] == -1 means the term with that index
			 * is not relevant for the current sorting.
			 */
		    if(this.sortIndexes[i] != -1){
		    	comparison = t0.getTerm(i).compareTo(
						t1.getTerm(this.sortIndexes[i]));
				if(comparison != 0){
					return comparison;
				}
			}
		}
		/*
		 * If two tuples are equal comparing them on the sort indexes
		 * then compare them in an ascending order
		 */
		for(int i=0; i<this.sortIndexes.length; i++){
		 	comparison = t0.getTerm(i).compareTo(
					t1.getTerm(i));
			if((comparison | equal) != equal){
				// not equal
				return comparison;
			}
		}
		return comparison;
	}
	
	/**
	 * Compares its two arguments for order. Comparison is based on 
	 * the sort indexes. Returns a negative integer, zero, or a positive
	 * integer as the first argument is less than, equal to, or greater 
	 * than the second.
	 * joinCompare is a compare method used for comparing two tuples in
	 * the Join operation.
	 * 
	 * @param t0
	 * 			 - the first tuple to be compared.
	 * @param t1
	 * 			 - the second tuple to be compared.
	 * @return
	 * 			 - a negative integer, zero, or a positive integer as 
	 * 			   the first argument is less than, equal to, or greater 
	 * 			   than the second.
	 */
	public int joinCompare(ITuple t0, ITuple t1) {
		int comparison = 0;
		for(int i=0; i<this.sortIndexes.length; i++){
			if(this.sortIndexes[i] != -1){
		    	comparison = t0.getTerm(i).compareTo(
						t1.getTerm(this.sortIndexes[i]));
				if(comparison != 0){
					return comparison;
				}
			}
		}
		return 0;
	}

	public int[] getSortIndexes() {
		return this.sortIndexes;
	}

	public void setSortIndexes(final int[] sortsortIndexes) {
		this.sortIndexes = sortIndexes;
	}
	
	private int[] getDefaultSortIndexes(int arity){
		int[] indexes = new int[arity];
		for(int i=0; i<arity; i++){
			indexes[i] = i;
		}
		return indexes;
	}
}