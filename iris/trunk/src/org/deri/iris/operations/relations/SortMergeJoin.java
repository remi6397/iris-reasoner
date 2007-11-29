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
import org.deri.iris.api.operations.relation.IMixedDatatypeRelationOperation;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * A sort-merge-join implementation. This implementation joins two
 * <code>IMixedDatatypeRelation</code> which must implement the
 * <code>indexOn(Integer[])</code> method.
 * </p>
 * <p>
 * The join indexes are specified as follows: the lenght of the index array is
 * equal to the first relation. The numbers in the denote the indexes of the
 * terms in the second relation with which the terms of the
 * first relation will be compared. To compare the terms every not-negative
 * number of the index array will be taken and the term of the first relation at
 * the index (starting from 0) of the number in the index array will be compared
 * to the term for the second relation at the number's index (starting at 0).
 * E.g. the first relation has arity 4, the second one 6 and the index array is
 * <code>[-1, 5, 1, 3]</code>. Then the second term of the first relation will
 * be compared with the 6th of the second one, the 3rd of the first with the
 * second of the second, and the 4th of the first with the 4th of the second
 * according to the <code>JoinCondition</code>.
 * </p>
 * <p>
 * $Id: SortMergeJoin.java,v 1.8 2007-10-19 07:37:18 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.8 $
 */
public class SortMergeJoin implements IMixedDatatypeRelationOperation, IJoin {

	/** The outher relation. */
	private final IMixedDatatypeRelation r0;

	/** The inner relation. */
	private final IMixedDatatypeRelation r1; 

	/** The join index array. */
	private final int[] idx;

	/** The join condition which must be met by tuples to join. */
	private final JoinCondition c;

	/**
	 * Which semi-join should be performed.
	 * A <code>0</code> means, that only the tuples
	 * of the first relation (<code>r0</code>) are taken. A <code>1</code>
	 * means, that only tuples from the second (<code>r1</code>) relation
	 * are taken. A negative number means, that tuples of all relations
	 * should be concated.
	 */
	private final int semiJoin;

	/**
	 * Constructs a new join operation.
	 * @param r0 the outer relation
	 * @param r1 the inner relation
	 * @param idx the join indexes (see class description)
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
	SortMergeJoin(final IMixedDatatypeRelation r0, final IMixedDatatypeRelation r1, final int[] idx, 
			final JoinCondition c) {
		this(r0, r1, idx, c, -1);
	}

	/**
	 * <p>
	 * Constructs a new join operation.
	 * </p>
	 * <p>
	 * This constructor also takes a number to determine whether a semi join
	 * should be done, or not. A <code>0</code> means, that only the tuples
	 * of the first relation (<code>r0</code>) are taken. A <code>1</code>
	 * means, that only tuples from the second (<code>r1</code>) relation
	 * are taken. A negative number means, that tuples of all relations
	 * should be concated.
	 * </p>
	 * @param r0 the outer relation
	 * @param r1 the inner relation
	 * @param idx the join indexes like the ones definded in {@link
	 * org.deri.iris.api.factory.IRelationOperationsFactory
	 * IRelationOperationsFactory}
	 * @param c the join condition if it is <code>null</code> it will be
	 * <code>JoinCondition.EQUALS</code>
	 * @param semiJoin denotes whether a semi join should be done and which
	 * relation should be taken
	 * @throws NullPointerException if one of the relations is
	 * <code>null</code>
	 * @throws NullPointerException if the index array is <code>null</code>
	 * @throws IllegalArgumentException if the length of the index array is
	 * unequal to the arity of the outer relation
	 * @throws IllegalArgumentException if a index of the index
	 * array is equal or greater than the arity of the inner relation
	 */
	SortMergeJoin(final IMixedDatatypeRelation r0, final IMixedDatatypeRelation r1, final int[] idx, 
			final JoinCondition c, final int semiJoin) {
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
		this.semiJoin = semiJoin;
	}

	/**
	 * Determines the indexes the relations should <code>indexOn</code>.
	 * @return array with the sortindexes for the first relation at index
	 * <code>[0]</code> and the indexes for the second relation at index
	 * <code>[1]</code>
	 */
	private int[][] sortIndexes() {
		final int[][] res = new int[2][];
		res[0] = new int[r0.getArity()];
		res[1] = new int[r1.getArity()];
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
				terms[idx[i]] = outer.get(i);
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
	private ITuple concat(final ITuple... t) {
		if (t == null) {
			throw new NullPointerException("The tuple array must not be null");
		}
		if (semiJoin >= 0) {
			return t[semiJoin];
		}
		final List<ITerm> terms = new LinkedList<ITerm>();
		for (final ITuple tup : t) {
			if (tup == null) {
				throw new NullPointerException("None of the tuples must be null");
			}
			terms.addAll(tup);
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
					matches = (idx[i] > -1) ? t0.get(i).equals(t1.get(idx[i])) : true;
				}
				break;
			case LESS_THAN:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.get(i).compareTo(t1.get(idx[i])) > 0 : true;
				}
				break;
			case GREATER_THAN:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.get(i).compareTo(t1.get(idx[i])) < 0 : true;
				}
				break;
			case LESS_OR_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.get(i).compareTo(t1.get(idx[i])) >= 0 : true;
				}
				break;
			case GREATER_OR_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? t0.get(i).compareTo(t1.get(idx[i])) <= 0 : true;
				}
				break;
			case NOT_EQUAL:
				for (int i = 0; (i < idx.length) && matches; i++) {
					matches = (idx[i] > -1) ? !t0.get(i).equals(t1.get(idx[i])) : true;
				}
				break;
			default:
				throw new IllegalArgumentException(
						"Couldn't handle the case " + c);
		}
		return matches;
	}

	public IMixedDatatypeRelation evaluate() {
		// sort
		final int[][] sortIndexes = sortIndexes();
		final IMixedDatatypeRelation sr0 = r0.indexOn(sortIndexes[0]);
		final IMixedDatatypeRelation sr1 = r1.indexOn(sortIndexes[1]);
		// merge
		final IMixedDatatypeRelation res;
		if (semiJoin == 0) {
			res =  RELATION.getMixedRelation(r0.getArity());
		} else if (semiJoin == 1) {
			res =  RELATION.getMixedRelation(r1.getArity());
		} else {
			res =  RELATION.getMixedRelation(r0.getArity() + r1.getArity());
		}
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
	
	public IMixedDatatypeRelation join() {
		return this.evaluate();
	}
}
