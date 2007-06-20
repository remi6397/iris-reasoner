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

import java.util.Arrays;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IComparator;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Basic tuple comparator.
 * </p>
 * <p>
 * $Id: BasicComparator.java,v 1.7 2007-04-10 13:41:43 poettler_ric Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.7 $
 */
public class BasicComparator implements IComparator {

	private int[] sortIndexes;

	private int arity;
	
	
	public BasicComparator() {
	}
	
	/**
	 * Default comparator - ascending order
	 * 
	 * @param arity
	 * 				of tuples that will be compared
	 */
	public BasicComparator(int arity) {
		this.sortIndexes = this.getDefaultSortIndexes(arity);
		this.arity = arity;
	}

	/**
	 * Comparator defined by sort indexes.
	 * 
	 * Note: The length of the sortIndexes needs to be equal to
	 * the arity of tuples that will be compared using 
	 * the comparator.
	 * 
	 * @param si are sort indexes for the comparison.
	 */
	public BasicComparator(final int[] si) {
		this.sortIndexes = si;
		this.arity = si.length;
	}

	/**
	 * Compares its two arguments for order. Comparison is based on 
	 * the sort indexes. Returns a negative integer, zero, or a positive
	 * integer if the first argument is less than, equal to, or greater 
	 * than the second. If two tuples are equal, comparing them on 
	 * the sort indexes, then compare them in an ascending order.
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
		if ((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The Tuples must not be null");
		}

		// copying frequently accessed fields
		final List<ITerm> t0t = t0.getTerms();
		final List<ITerm> t1t = t1.getTerms();

		if (t0t.size() != t1t.size()) {
			throw new IllegalArgumentException(
					"Couldn't compare due to different arity of tuples.");
		}
		if (t0t.size() != sortIndexes.length) {
			throw new IllegalArgumentException(
					"The length of sort indexe array does not match " +
					"the arity of the compared tuples.");
		} 
		int comparison = 0;
		if (sortIndexes == null) {
			sortIndexes = getDefaultSortIndexes(t0t.size());
		}
		for(int i = 0; i < sortIndexes.length; i++) {
			/* compare tuples on each index that is differnt from -1.
			 * sortIndexes[i] == -1 means the term with that index
			 * is not relevant for the current sorting.
			 */
			if(sortIndexes[i] != -1) {
				comparison = t0t.get(i).compareTo(t1t.get(sortIndexes[i]));
				if(comparison != 0){
					return comparison;
				}
			}
		}
		/*
		 * If two tuples are equal comparing them on the sort indexes
		 * then compare them in an ascending order
		 */
		for(int i = 0; i < sortIndexes.length; i++) {
		 	comparison = t0t.get(i).compareTo(t1t.get(i));
			if(comparison != 0){	
		 		// not equal
				return comparison;
			}
		}
		return comparison;
	}

	public int[] getSortIndexes() {
		return this.sortIndexes;
	}

	public void setSortIndexes(final int[] sortIndexes) {
		this.sortIndexes = sortIndexes;
	}
	
	private int[] getDefaultSortIndexes(int arity){
		int[] indexes = new int[arity];
		Arrays.fill(indexes, -1);
		return indexes;
	}

	public int getArity() {
		return this.arity;
	}
}
