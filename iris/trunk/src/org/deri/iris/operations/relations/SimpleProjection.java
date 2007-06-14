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

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IMixedDatatypeRelationOperation;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * A simple projection implementation.
 * </p>
 * <p>
 * The index array is contructed as follows: The numbers in the index array
 * represent the columns of the relation and determine, where their terms should
 * be put after the projection. Columns with a negative number will be left out
 * in the resulting relation. The other columns will be put in the order from
 * the smallest to the highest number, from left to right. E.g. an index of
 * <code>[-1, 5, 3 -1, 10]</code> will produce a relation with the column at
 * index 2 (starting from 0) will be put at index 0, the column at index 1 will
 * be put at index 1 and the column at index 4 will be put at index 2. All other
 * columns will be left out.
 * </p>
 * <p>
 * $Id: SimpleProjection.java,v 1.1 2007-06-14 15:54:34 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class SimpleProjection implements IMixedDatatypeRelationOperation {

	/** Relation from which to project. */
	private final IMixedDatatypeRelation r;

	/** Projection index. */
	private final int[] idx;

	/**
	 * Creates a new porjection.
	 * @param r the relation from which to project
	 * @param idx the projection index (see class description)
	 * @throws NullPointerException if the relation is <code>null</code>
	 * @throws NullPointerException if the idx is <code>null</code>
	 * @throws IllegalArgumentException if the lenght of the index and the
	 * arity of the relation doesn't match
	 */
	SimpleProjection(final IMixedDatatypeRelation r, final int[] idx) {
		if (r == null) {
			throw new NullPointerException("The relation must not be null");
		}
		if (idx == null) {
			throw new NullPointerException("The index must not be null");
		}
		if (idx.length != r.getArity()) {
			throw new IllegalArgumentException("The length of the index array (" + 
					idx.length + ") and the arity of the relation (" + 
					r.getArity() + ") must match");
		}
		this.r = r;
		this.idx = idx;
	}

	/**
	 * <p>
	 * Returns an optimized array to determine which term to put at which
	 * position in an efficient way.
	 * </p>
	 * <p>
	 * The returned array is constructed as follows: The number at index 0
	 * is the index of the term of the tuple gained from the relation which 
	 * in the end should be put at index 0 in the tuple of the resulting 
	 * relation. And so on...
	 * </p>
	 * @return the optimized array
	 */
	private int[] optimizeIndexes() {
		final int[] sorted = Arrays.copyOfRange(idx, 0, idx.length);
		Arrays.sort(sorted);
		final int[] tmp = new int[idx.length];
		int j = 0;
		for (final int i : sorted) {
			if (i < 0) {
				continue;
			}
			tmp[j++] = indexOf(idx, i);
		}
		return Arrays.copyOfRange(tmp, 0, j);
	}

	/**
	 * Searches for the first orccurence of needle in stack.
	 * @param stack the stack which to search through
	 * @param needle the element to look for
	 * @return the index of the first occurence of <code>needle</code> in
	 * <code>stack</code>
	 * @throws NullPointerException if the stack is <code>null</code>
	 */
	private int indexOf(final int[] stack, final int needle) {
		if (stack == null) {
			throw new NullPointerException("The stack must not be null");
		}
		for (int i = 0; i < stack.length; i++) {
			if (needle == stack[i]) {
				return i;
			}
		}
		return -1;
	}

	public IMixedDatatypeRelation evaluate() {
		final int[] optimized = optimizeIndexes();
		final IMixedDatatypeRelation res = RELATION.getMixedRelation(optimized.length);
		for (final ITuple t : r) {
			final List<ITerm> tmp = new LinkedList<ITerm>();
			for (int i : optimized) {
				tmp.add(t.getTerm(i));
			}
			res.add(BASIC.createTuple(tmp));
		}
		return res;
	}
}
