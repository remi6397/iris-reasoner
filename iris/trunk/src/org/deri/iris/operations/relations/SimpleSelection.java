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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.operations.relation.IMixedDatatypeRelationOperation;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Selection implementation using semi-joins for selecting the tuples.
 * </p>
 * <p>
 * There are 2 ways to select tuples: <b>relative</b> and <b>absolute</b>.
 * </p>
 * <h5>relative:</h5>
 * <p>
 * With the relative selection it is only possible to select tuples with the
 * same value at some predefined positions. To do this, two equal numbers are
 * put into the index array. E.g. <code>[0, 1, 1, 0]</code> would select all
 * tuples where the term at index 1 and 2 (starting from 0) are equal. An index
 * of <code>[11, 0, 11, 0]</code> would select all tuples where the terms at
 * index 0 and 2 are equal.
 * </p>
 * <h5>absolute:</h5>
 * <p>
 * In the absolute selection all tuples are selected meeting some special
 * condition defined by a condition operator and a threshold. E.g. an index of
 * <code>[0, 1, 0, 0]<code>, a term array of <code>[3]</code> and a condition
 * array of <code>[LESS_THAN]</code> would select all tuples where the term at index
 * 1 (starting from 0) is less than 3. An index array of <code>[1, 0, 0,
 * 2]</code>, a term array of <code>[5, k]</code> and a condition array of
 * <code>[GREATER_THAN, LESS_OR_EQUAL]</code> would select all terms where the
 * term at index 0 is greater than 5 and the term at index 3 is less or equal
 * than k.
 * </p>
 * <p>
 * The both types can be done in one selection. Numbers less or equal to 0 will
 * be ignored in the index array.
 * </p>
 * <p>
 * $Id: SimpleSelection.java,v 1.1 2007-06-13 15:00:47 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class SimpleSelection implements IMixedDatatypeRelationOperation {

	/** The relation to select from. */
	private final IMixedDatatypeRelation r;

	/** The index array defining what and how to select. */
	private final int[] idx;

	/** The threshold terms. */
	private final ITerm[] threshold;

	/** The conditions which should be met by the terms. */
	private final JoinCondition[] c;

	/**
	 * Constructs a new selection.
	 * @param r the relation from which to select from
	 * @param idx the index array (see class description)
	 * @param threshold the threshold values for the terms (see class description)
	 * @param c the condition array which defines how the thresholds must be
	 * met (see class description)
	 * @throws NullPointerException if the relation is <code>null</code>
	 * @throws NullPointerException if the index is <code>null</code>
	 * @throws NullPointerException if the threshold array is <code>null</code>
	 * @throws NullPointerException if the condition array is <code>null</code>
	 * @throws IllegalArgumentException if the condition array and the
	 * threshold array have different lengths
	 */
	SimpleSelection(final IMixedDatatypeRelation r, 
			final int[] idx, final ITerm[] threshold, final JoinCondition[] c) {
		if (r == null) {
			throw new NullPointerException("The relation must not be null");
		}
		if (idx == null) {
			throw new NullPointerException("The index must not be null");
		}
		if (threshold == null) {
			throw new NullPointerException("The threshold must not be null");
		}
		if (c == null) {
			throw new NullPointerException("The conditions must not be null");
		}
		if (threshold.length != c.length) {
			throw new IllegalArgumentException("The lenght of the thresholds (" + 
					threshold.length + ") and conditions (" + c.length + 
					") must be equal");
		}
		this.r = r;
		this.idx = idx;
		this.threshold = threshold;
		this.c = c;
	}

	/**
	 * Returns the indexes at which the needle occurs in the stack.
	 * @param stack through wich to search
	 * @param needle for what to look for
	 * @return the indexes where <code>needle</code> occurs in
	 * <code>stack</code>
	 * @throws NullPointerException if <code>stack</code> is <code>null</code>
	 */
	private static int[] occurences(final int[] stack, final int needle) {
		if (stack == null) {
			throw new NullPointerException("The stack must not be null");
		}
		int[] tmp = new int[stack.length];
		int j = 0;
		for (int i = 0; i < stack.length; i++) {
			if (needle == stack[i]) {
				tmp[j++] = i;
			}
		}
		int[] res = new int[j];
		System.arraycopy(tmp, 0, res, 0, j);
		return res;
	}

	/**
	 * Merges two join indexes. If both indexes have a positive number, then
	 * the value of <code>idx0</code> will be taken.
	 * @param idx0 the first index to merge
	 * @param idx1 the second index to merge
	 * @return the merged index
	 * @throws NullPointerException if one index is <code>null</code>
	 */
	private static int[] mergeJoinIndexes(final int[] idx0, final int[] idx1) {
		if ((idx0 == null) || (idx1 == null)) {
			throw new NullPointerException("The indexes must not be null");
		}
		int[] res = new int[idx1.length];
		System.arraycopy(idx1, 0, res, 0, idx1.length);
		for (int i = 0; i < idx0.length; i++) {
			if (idx0[i] > -1) {
				res[i] = idx0[i];
			}
		}
		return res;
	}

	public IMixedDatatypeRelation evaluate() {
		IMixedDatatypeRelation res = null;
		// getting the index groups on which we should select
		final Set<Integer> todo = new HashSet();
		for (int i = 0; i < idx.length; i++) {
			todo.add(i);
		}
		final List<int[]> selectOn = new ArrayList<int[]>(idx.length);
		while (!todo.isEmpty()) {
			final int i = todo.iterator().next();
			if (idx[i] <= 0) {
				todo.remove(i);
				continue;
			}
			final int[] found = occurences(idx, idx[i]);
			selectOn.add(found);
			for (int j : found) {
				todo.remove(j);
			}
		}
		// selecting the indexes for the self join
		int[] jidx0 = new int[r.getArity()];
		Arrays.fill(jidx0, -1);
		boolean doJoin = false;
		for (final int[] i : selectOn) {
			if (i.length > 1) {
				final int[] tmpidx = new int[r.getArity()];
				Arrays.fill(tmpidx, -1);
				for (int j = 0; j < i.length; j++) {
					tmpidx[i[j]] = i[0];
				}
				jidx0 = mergeJoinIndexes(tmpidx, jidx0);
				doJoin = true;
			}
		}
		if (doJoin) {
			res = (new SortMergeJoin(r, r, jidx0, JoinCondition.EQUALS, 0)).evaluate();
		}
		// selecting for the remaining thresholds
		int j = 0;
		for (final int[] i : selectOn) {
			if (i.length == 1) {
				final IMixedDatatypeRelation tmp = RELATION.getMixedRelation(r.getArity());
				// constructing the minimal tuple
				final ITerm[] minTerms = new ITerm[r.getArity()];
				Arrays.fill(minTerms, null);
				minTerms[0] = threshold[j];
				tmp.add(BASIC.createTuple(minTerms));
				// constructing the join index
				final int[] jidx1 = new int[r.getArity()];
				Arrays.fill(jidx1, -1);
				jidx1[0] = i[0];

				if (res == null) { // add matching tuples
					res = (new SortMergeJoin(tmp, r, jidx1, c[j], 1)).evaluate();
				} else { // remove not matching tuples
					res.retainAll((new SortMergeJoin(tmp, res, jidx1, c[j], 1)).evaluate());
				}
				j++;
			}
		}
		return res;
	}
}
