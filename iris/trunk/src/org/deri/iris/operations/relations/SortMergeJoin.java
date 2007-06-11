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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * A sort-merge-join implementation. This implementation joins two
 * <code>IMixedDatatypeRelation</code> which must implement the
 * <code>indexOn(Integer[])</code> method.
 * </p>
 * <p>
 * $Id: SortMergeJoin.java,v 1.2 2007-06-11 12:06:52 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.2 $
 */
public class SortMergeJoin implements IJoin {

	/** The outher relation. */
	private final IMixedDatatypeRelation r0;

	/** The inner relation. */
	private final IMixedDatatypeRelation r1; 

	/**
	 * The join index array. 
	 * @see org.deri.iris.api.factory.IRelationOperationsFactory
	 */
	private final int[] idx;

	/** The join condition which must be met by tuples to join. */
	private final JoinCondition c;

	/**
	 * Constructs a new join operation.
	 * @param r0 the outer relation
	 * @param r1 the inner relation
	 * @param idx the join indexes like the ones definded in {@link
	 * org.deri.iris.api.factory.IRelationOperationsFactory
	 * IRelationOperationsFactory}
	 * @param c the join condition if it is <code>null</code> it will be
	 * <code>JoinCondition.EQUALS</code>
	 * @throws NullPointerException if one of the relations is
	 * <code>null</code>
	 * @throws NullPointerException if the index array is <code>null</code>
	 * @throws IllegalArgumentException if the length of the index array is
	 * unequal to the arity of the outer relation
	 * @throws IllegalArgumentException if a index of the index
	 * array is equal or greater than the arity of the inner relation
	 */
	SortMergeJoin(final IMixedDatatypeRelation r0, final IMixedDatatypeRelation r1, final int[] idx, final JoinCondition c) {
		if ((r0 == null) || (r1 == null)) {
			throw new NullPointerException("The relations must not be null");
		}
		if (idx == null) {
			throw new NullPointerException("The joinindex must not be null");
		}
		if (idx.length != r0.getArity()) {
			throw new IllegalArgumentException("The lenght of the index array (" + 
					idx.length + ") must match the arity of the first relation (" + 
					r0.getArity() + ")");
		}
		for (final int i : idx) {
			if (i > r1.getArity()) {
				throw new IllegalArgumentException("The indexes " + Arrays.toString(idx) + 
						" must not be greater than the arity of the second relation: " + 
						r1.getArity());
			}
		}
		this.r0 = r0;
		this.r1 = r1;
		this.idx = idx;
		this.c = (c == null) ? JoinCondition.EQUALS : c;
	}

	/**
	 * Determines the indexes the relations should <code>indexOn</code>.
	 * @return array with the sortindexes for the first relation at index
	 * <code>[0]</code> and the indexes for the second relation at index
	 * <code>[1]</code>
	 */
	private Integer[][] sortIndexes() {
		final Integer[][] res = new Integer[2][];
		res[0] = new Integer[r0.getArity()];
		res[1] = new Integer[r1.getArity()];
		Arrays.fill(res[0], 0);
		Arrays.fill(res[1], 0);
		int j = 1;
		for (int i = 0; i < idx.length; i++) {
			if (idx[i] >= 0) {
				res[0][i] = j;
				res[1][idx[i]] = j;
				j++;
			}
		}
		return res;
	}

	/**
	 * Creates the minimal tuple for the inner relation according to a tuple
	 * of the outer one.
	 * @param outer the tuple of the outer relation
	 * @return the minimal tuple for the inner relation
	 * @throws NullPointerException if the tuple is <code>null</code>
	 */
	private ITuple minimalTupleForInner(final ITuple outer) {
		if (outer == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final ITerm[] terms = new ITerm[r1.getArity()];
		Arrays.fill(terms, null);
		for (int i = 0; i < idx.length; i++) {
			if (idx[i] >= 0) {
				terms[idx[i]] = outer.getTerm(i);
			}
		}
		return BASIC.createTuple(terms);
	}

	/**
	 * Concats the terms of tuples to one tuple.
	 * @param t the tuples whose terms should be concated
	 * @return the resulting tuple
	 * @throws NullPointerException if one tuple is <code>null</code>
	 */
	private static ITuple concat(final ITuple... t) {
		if (t == null) {
			throw new NullPointerException("The tuple array must not be null");
		}
		final List<ITerm> terms = new LinkedList<ITerm>();
		for (final ITuple tup : t) {
			if (tup == null) {
				throw new NullPointerException("None of the tuples must be null");
			}
			terms.addAll(tup.getTerms());
		}
		return BASIC.createTuple(terms);
	}

	/**
	 * Checks whether the two Tuples would match the
	 * <code>JoinCondition</code>.
	 * @param t0 the tuple from the outer relation
	 * @param t1 the tuple from the inner relation
	 * @return <code>true</code> if the tuples meet the
	 * <code>JoinCondition</code>, otherwise <code>false</code>
	 * @throws NullPointerException if one of the tuple is
	 * </code>null</code>
	 */
	public boolean matchesCondition(final ITuple t0, final ITuple t1) {
		if ((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The tuples must not be null");
		}
		boolean matches = true;
		switch (c) {
			case EQUALS:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.getTerm(i).equals(t1.getTerm(idx[i])) : true;
				}
				break;
			case LESS_THAN:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.getTerm(i).compareTo(t1.getTerm(idx[i])) > 0 : true;
				}
				break;
			case GREATER_THAN:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.getTerm(i).compareTo(t1.getTerm(idx[i])) < 0 : true;
				}
				break;
			case LESS_OR_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.getTerm(i).compareTo(t1.getTerm(idx[i])) >= 0 : true;
				}
				break;
			case GREATER_OR_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.getTerm(i).compareTo(t1.getTerm(idx[i])) <= 0 : true;
				}
				break;
			case NOT_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? !t0.getTerm(i).equals(t1.getTerm(idx[i])) : true;
				}
				break;
			default:
				throw new IllegalArgumentException(
						"Couldn't handle the case " + c);
		}
		return matches;
	}

	public IRelation join() {
		// sort
		final Integer[][] sortIndexes = sortIndexes();
		final IMixedDatatypeRelation sr0 = r0.indexOn(sortIndexes[0]);
		final IMixedDatatypeRelation sr1 = r1.indexOn(sortIndexes[1]);
		// merge
		final IRelation res =  RELATION.getMixedRelation(r0.getArity() + r1.getArity());
		if ((c == JoinCondition.LESS_THAN) || 
				(c == JoinCondition.LESS_OR_EQUAL) || 
				(c == JoinCondition.NOT_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedHeadSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(concat(t0, t1));
						} else {
							break;
						}
					}
				}
			}
		}
		if ((c == JoinCondition.LESS_OR_EQUAL) || 
				(c == JoinCondition.EQUALS) || 
				(c == JoinCondition.GREATER_OR_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedTailSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(concat(t0, t1));
						} else {
							break;
						}
					}
				}
			}
		}
		if ((c == JoinCondition.GREATER_THAN) || 
				(c == JoinCondition.GREATER_OR_EQUAL) || 
				(c == JoinCondition.NOT_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedTailSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(concat(t0, t1));
						}
					}
				}
			}
		}
		return res;
	}
}
