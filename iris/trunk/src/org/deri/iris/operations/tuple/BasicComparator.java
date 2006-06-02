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

import java.util.Comparator;

import org.deri.iris.api.basics.ITuple;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 15:10:34
 */
public abstract class BasicComparator implements Comparator<ITuple> {

	private int[] sortIndexes;

	public BasicComparator() {
	}
	
	/**
	 * Default comparator - ascending order
	 * 
	 * @param arity
	 * 				of tuples that will be compared
	 */
	/*public BasicComparator(int arity) {
		this.sortIndexes = this.getDefaultSortIndexes(arity);
	}*/

	/**
	 * Comparator defined by sort indexes
	 * 
	 * @param sortIndexes
	 */
	public BasicComparator(final int[] sortIndexes) {
		this.sortIndexes = sortIndexes;
	}

	/**
	 * This method is supposed to be overridden by a comparator 
	 * implementation that extends this class. 
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
	public int compare(ITuple t0, ITuple t1){
		return 0;
	}

	public int[] getSortIndexes() {
		return this.sortIndexes;
	}

	public void setSortIndexes(final int[] sortIndexes) {
		this.sortIndexes = sortIndexes;
	}
	
	/*private int[] getDefaultSortIndexes(int arity){
		int[] indexes = new int[arity];
		for(int i=0; i<arity; i++){
			indexes[i] = i;
		}
		return indexes;
	}*/
}
