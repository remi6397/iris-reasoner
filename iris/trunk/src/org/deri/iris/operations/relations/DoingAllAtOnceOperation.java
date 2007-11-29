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
import java.util.NoSuchElementException;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * A strange join implementation. This implementation does first a slection on
 * both relations, then joins them and while merging the tuples it does a
 * projection.
 * </p>
 * <p>
 * How the selection parameters are specified are documented in the {@link
 * SimpleSelection SimpleProjection}.
 * <p>
 * <p>
 * How the join parameters are specified are documented in the {@link
 * SortMergeJoin SortMergeJoin}.
 * <p>
 * <p>
 * How the projection parameters are specified are documented in the {@link
 * SimpleProjection SimpleProjection}.
 * <p>
 * $Id: DoingAllAtOnceOperation.java,v 1.3 2007-10-19 07:37:18 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.3 $
 * @see SortMergeJoin
 * @see SimpleSelection
 * @see SimpleProjection
 */
/*
 * authors's note: i totally disagree with this implementation, since i think,
 * that it should be separated into distinct classes where each
 * operation is handeled on its own. this class is only written because
 * possible (not proven) performance improfements seem to be more importatnt than code
 * readablility/maintainability and the fact, that i'm tired of discussing. 
 *
 * so don't blame me, if you're not able to read the code!
 * i won't explain the code to anyone. read it - it is documented!
 */
public class DoingAllAtOnceOperation implements IJoin {

	/** The outher relation. */
	private final IMixedDatatypeRelation r0;

	/** The inner relation. */
	private final IMixedDatatypeRelation r1; 

	/**
	 * The join index array. 
	 * @see SortMergeJoin
	 */
	private final int[] jidx;

	/**
	 * The join condition which must be met by tuples to join. 
	 * @see SortMergeJoin
	 */
	private final JoinCondition jc;

	/**
	 * Which semi-join should be performed.
	 * @see SortMergeJoin
	 */
	private final int semiJoin;

	/**
	 * Projection index.
	 * @see SimpleProjection
	 */
	private final int[] pidx;

	/**
	 * The optimized projection indexes. 
	 * @see #optimizeIndexes()
	 */
	private int[] optimizedPidx = null;

	/**
	 * The selection indexes for the first relation. 
	 * @see SimpleSelection
	 */
	private final int[] sidx0;

	/**
	 * The selection indexes for the second relation.
	 * @see SimpleSelection
	 */
	private final int[] sidx1;

	/**
	 * The selection conditions for the first relation.
	 * @see SimpleSelection
	 */
	private final JoinCondition[] sc0;

	/**
	 * The selection conditions for the second relation.
	 * @see SimpleSelection
	 */
	private final JoinCondition[] sc1;

	/**
	 * The selection thresholds for the first relation
	 * @see SimpleSelection
	 */
	private final ITerm[] sthreshold0;

	/**
	 * The selection thresholds for the second relation.
	 * @see SimpleSelection
	 */
	private final ITerm[] sthreshold1;

	/**
	 * <p>
	 * Constructs a new monster operation.
	 * </p>
	 * <p>
	 * NOTE: if <code>pidx</code> and <code>semiJoin</code> (only
	 * recognized, if positive) are given, then only a semi-join will be
	 * done.
	 * </p>
	 * @param r0 the outer relation
	 * @param sidx0 the selection indexes for the first relation. Might be
	 * <code>null</code> if no selection should be done
	 * @param sthreshold0 the threshold terms for the first relation
	 * @param sc0 the selection conditions for the first relation
	 * @param r1 the inner relation
	 * @param sidx1 the selection indexes for the second relation. Might be
	 * <code>null</code> if no selection should be done
	 * @param sthreshold1 the threshold terms for the second relation
	 * @param sc1 the selection conditions for the second relation
	 * @param jidx the join indexes
	 * @param jc the join conditions
	 * @param semiJoin whether a semijoin should be made (has a higer
	 * priority than the projection indexes
	 * @param pidx the projection indexes. Might be <code>null</code> if no
	 * projection should be made
	 * @throws NullPointerException if sthreshold0 is <code>null</code> and
	 * sidx0 is not <code>null</code>
	 * @throws NullPointerException if sc0 is <code>null</code> and
	 * sidx0 is not <code>null</code>
	 * @throws IllegalArgumentException if the length of sthreshold0 is
	 * unequal to the lenght of sc0 and sidx0 is not <code>null</code>
	 * @throws NullPointerException if sthreshold1 is <code>null</code> and
	 * sidx1 is not <code>null</code>
	 * @throws NullPointerException if sc1 is <code>null</code> and
	 * sidx1 is not <code>null</code>
	 * @throws IllegalArgumentException if the length of sthreshold1 is
	 * unequal to the lenght of sc1 and sidx1 is not <code>null</code>
	 * @throws NullPointerException if r0 or r1 is <code>null</code>
	 * @throws NullPointerException if jidx is <code>null</code>
	 * @throws IllegalArgumentException if the length of jidx is unequal to
	 * the arity of the first relation
	 * @throws IllegalArgumentException if one of the numbers in jidx is
	 * greater of equal to the arity of the second relation
	 */
	DoingAllAtOnceOperation(final IMixedDatatypeRelation r0, final int[] sidx0, final ITerm[] sthreshold0, final JoinCondition[] sc0, 
			final IMixedDatatypeRelation r1, final int[] sidx1, final ITerm[] sthreshold1, final JoinCondition[] sc1, 
			final int[] jidx, final JoinCondition jc, final int semiJoin, final int[] pidx) {
		if (sidx0 != null) { // assert and assign the parameters for the selection of r0
			if (sthreshold0 == null) {
				throw new NullPointerException("The first selection thresholds must not be null");
			}
			if (sc0 == null) {
				throw new NullPointerException("The first slection conditions must not be null");
			}
			if (sthreshold0.length != sc0.length) {
				throw new IllegalArgumentException("The lenght of the first selection thresholds (" + 
						sthreshold0.length + ") and first selection conditions (" + sc0.length + 
						") must be equal");
			}
			this.sidx0 = sidx0;
			this.sthreshold0 = sthreshold0;
			this.sc0 = sc0;
		} else {
			this.sidx0 = null;
			this.sthreshold0 = null;
			this.sc0 = null;
		}
		if (sidx1 != null) { // assert and assign the parameters for the selection of r1
			if (sthreshold1 == null) {
				throw new NullPointerException("The second selection thresholds must not be null");
			}
			if (sc1 == null) {
				throw new NullPointerException("The second slection conditions must not be null");
			}
			if (sthreshold1.length != sc1.length) {
				throw new IllegalArgumentException("The lenght of the second selection thresholds (" + 
						sthreshold1.length + ") and second selection conditions (" + sc1.length + 
						") must be equal");
			}
			this.sidx1 = sidx1;
			this.sthreshold1 = sthreshold1;
			this.sc1 = sc1;
		} else {
			this.sidx1 = null;
			this.sthreshold1 = null;
			this.sc1 = null;
		}
		// assert the normal join parameters
		if ((r0 == null) || (r1 == null)) {
			throw new NullPointerException("The relations must not be null");
		}
		if (jidx == null) {
			throw new NullPointerException("The joinindex must not be null");
		}
		if (jidx.length != r0.getArity()) {
			throw new IllegalArgumentException("The lenght of the join index array (" + 
					jidx.length + ") must match the arity of the first relation (" + 
					r0.getArity() + ")");
		}
		for (final int i : jidx) {
			if (i > r1.getArity()) {
				throw new IllegalArgumentException("The join indexes " + Arrays.toString(jidx) + 
						" must not be greater than the arity of the second relation: " + 
						r1.getArity());
			}
		}
		this.r0 = r0;
		this.r1 = r1;
		this.jidx = jidx;
		this.jc = (jc == null) ? JoinCondition.EQUALS : jc;
		this.semiJoin = semiJoin;
		this.pidx = pidx;
	}

	/**
	 * <p>
	 * Returns an optimized array, for the projection, to determine which term to put at which
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
		final int[] sorted = new int[pidx.length];
		System.arraycopy(pidx, 0, sorted, 0, pidx.length);
		Arrays.sort(sorted);
		final int[] tmp = new int[pidx.length];
		int j = 0;
		for (final int i : sorted) {
			if (i < 0) {
				continue;
			}
			tmp[j++] = indexOf(pidx, i);
		}
		final int[] res = new int[j];
		System.arraycopy(tmp, 0, res, 0, j);
		return res;
	}

	/**
	 * Searches for the first orccurence of needle in stack.
	 * @param stack the stack which to search through
	 * @param needle the element to look for
	 * @return the index of the first occurence of <code>needle</code> in
	 * <code>stack</code>
	 * @throws NullPointerException if the stack is <code>null</code>
	 */
	private static int indexOf(final int[] stack, final int needle) {
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

	/**
	 * <p>
	 * Retrieves a term at a given position out of a tuple array. If the
	 * index is too big, a <code>NoSuchElementException</code> will be
	 * thrown.
	 * </p>
	 * <p>
	 * E.g. we have 3 tuples of arity 4, 2 and 3. We want to retrieve the
	 * last term of the second tuple (with arity 2) we define an index of 5,
	 * if we want the second term of the last tuple we would pass in an
	 * index of 7.
	 * </p>
	 * @param idx the index of the term to retrieve
	 * @param t the tuples from where to pick the term
	 * @return the picked term
	 * @throws IllegalArgumentException if the index is negative
	 * @throws NullPointerException if the tuple array is <code>null</code>
	 * @throws NoSuchElementException if the index was greater or equal to
	 * the number of terms in all tuples.
	 */
	private static ITerm getTerm(final int idx, final ITuple... t) {
		if (idx < 0) {
			throw new IllegalArgumentException("The index must not be negative");
		}
		if (t == null) {
			throw new NullPointerException("The tuples must not be null");
		}
		int tmp = idx;
		for (final ITuple tup : t) {
			if (tmp < tup.size()) {
				return tup.get(tmp);
			} else {
				tmp -= tup.size();
			}
		}
		throw new NoSuchElementException("There was no term at position " + idx);
	}

	/**
	 * Projects a tuple according to an optimized index array.
	 * @param optimized the optimized index array
	 * @param t the tuple to project
	 * @return the projected tuple
	 * @throws NullPointerException if the tuple array is <code>null</code>
	 * @throws NullPointerException if the index array is <code>null</code>
	 * @see #optimizeIndexes();
	 */
	private static ITuple projectTuple(final int[] optimized, final ITuple... t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		if (optimized == null) {
			throw new NullPointerException("The optimized array must not be null");
		}
		final List<ITerm> tmp = new LinkedList<ITerm>();
		for (int i : optimized) {
			tmp.add(getTerm(i, t));
		}
		return BASIC.createTuple(tmp);
	}

	/**
	 * Determines the indexes the relations should <code>indexOn</code>
	 * according to the join idexes.
	 * @return array with the sortindexes for the first relation at index
	 * <code>[0]</code> and the indexes for the second relation at index
	 * <code>[1]</code>
	 */
	private int[][] sortIndexes() {
		final int[][] res = new int[2][];
		res[0] = new int[r0.getArity()];
		res[1] = new int[r1.getArity()];
		int j = 1;
		for (int i = 0; i < jidx.length; i++) {
			if (jidx[i] >= 0) {
				res[0][i] = j;
				res[1][jidx[i]] = j;
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
		for (int i = 0; i < jidx.length; i++) {
			if (jidx[i] >= 0) {
				terms[jidx[i]] = outer.get(i);
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
	private ITuple computeTuple(final ITuple... t) {
		if (t == null) {
			throw new NullPointerException("The tuple array must not be null");
		}
		if (semiJoin >= 0) {
			return t[semiJoin];
		}
		if (pidx != null) {
			if (optimizedPidx == null) {
				optimizedPidx = optimizeIndexes();
			}
			return projectTuple(optimizedPidx, t);
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
	 * <code>JoinCondition</code> of the join operation.
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
		switch (jc) {
			case EQUALS:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? t0.get(i).equals(t1.get(jidx[i])) : true;
				}
				break;
			case LESS_THAN:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? t0.get(i).compareTo(t1.get(jidx[i])) > 0 : true;
				}
				break;
			case GREATER_THAN:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? t0.get(i).compareTo(t1.get(jidx[i])) < 0 : true;
				}
				break;
			case LESS_OR_EQUAL:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? t0.get(i).compareTo(t1.get(jidx[i])) >= 0 : true;
				}
				break;
			case GREATER_OR_EQUAL:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? t0.get(i).compareTo(t1.get(jidx[i])) <= 0 : true;
				}
				break;
			case NOT_EQUAL:
				for (int i = 0; (i < jidx.length) && matches; i++) {
					matches = (jidx[i] > -1) ? !t0.get(i).equals(t1.get(jidx[i])) : true;
				}
				break;
			default:
				throw new IllegalArgumentException(
						"Couldn't handle the case " + jc);
		}
		return matches;
	}

	public IMixedDatatypeRelation evaluate() {
		// sort
		final int[][] sortIndexes = sortIndexes();
		final IMixedDatatypeRelation sr0;
		if (sidx0 != null) {
			sr0 = new SimpleSelection(r0, sidx0, sthreshold0, sc0).evaluate().indexOn(sortIndexes[0]);
		} else {
			sr0 = r0.indexOn(sortIndexes[0]);
		}
		final IMixedDatatypeRelation sr1;
		if (sidx1 != null) {
			sr1 = new SimpleSelection(r1, sidx1, sthreshold1, sc1).evaluate().indexOn(sortIndexes[1]);
		} else {
			sr1 = r0.indexOn(sortIndexes[1]);
		}
		// merge
		final IMixedDatatypeRelation res;
		if (semiJoin == 0) {
			res =  RELATION.getMixedRelation(r0.getArity());
		} else if (semiJoin == 1) {
			res =  RELATION.getMixedRelation(r1.getArity());
		} else if (pidx != null) {
			int pos = 0;
			for (final int i : pidx) {
				if (i >= 0) {
					pos++;
				}
			}
			res =  RELATION.getMixedRelation(pos);
		} else {
			res =  RELATION.getMixedRelation(r0.getArity() + r1.getArity());
		}
		if ((jc == JoinCondition.LESS_THAN) || 
				(jc == JoinCondition.LESS_OR_EQUAL) || 
				(jc == JoinCondition.NOT_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedHeadSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(computeTuple(t0, t1));
						} else {
							break;
						}
					}
				}
			}
		}
		if ((jc == JoinCondition.LESS_OR_EQUAL) || 
				(jc == JoinCondition.EQUALS) || 
				(jc == JoinCondition.GREATER_OR_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedTailSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(computeTuple(t0, t1));
						} else {
							break;
						}
					}
				}
			}
		}
		if ((jc == JoinCondition.GREATER_THAN) || 
				(jc == JoinCondition.GREATER_OR_EQUAL) || 
				(jc == JoinCondition.NOT_EQUAL)) {
			for (final ITuple t0 : sr0) {
				for (final SortedSet<ITuple> ss : sr1.separatedTailSet(minimalTupleForInner(t0))) {
					for (final ITuple t1 : ss) {
						if (matchesCondition(t0, t1)) {
							res.add(computeTuple(t0, t1));
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
