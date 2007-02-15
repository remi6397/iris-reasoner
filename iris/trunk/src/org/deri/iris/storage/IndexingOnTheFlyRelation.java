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

package org.deri.iris.storage;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;

/**
 * <p>
 * New relation which creates indexes on the fly.
 * </p>
 * <p>
 * All collections returned by this Relations are <b>unmodifiable</b>. If you
 * want to alter relation, you have to use the {@code add} and {@code remove}
 * method of this relation directly.
 * </p>
 * <p>
 * $Id: IndexingOnTheFlyRelation.java,v 1.1 2007-02-15 08:42:37 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.1 $
 */
public class IndexingOnTheFlyRelation extends AbstractSet<ITuple> implements
		SortedSet<ITuple>, IRelation {

	/** Map storing all created indexes and the corresponding relaions to them. */
	private final Map<Integer, SortedSet<ITuple>> indexes = new HashMap<Integer, SortedSet<ITuple>>();

	/** The primary index. */
	private SortedSet<ITuple> primary;

	/** The index array specifying on which the primary key is sorting. */
	private Integer[] primaryI;

	/** The arity of the relation. */
	private int arity;

	/**
	 * Creates a new empty relation object.
	 */
	IndexingOnTheFlyRelation() {
		initialize(0);
	}

	/**
	 * Initializes the relaiton for a given arity.
	 * 
	 * @param a
	 *            the arity for which to initialize the relation
	 * @throws IllegalArgumentException
	 *             if the arity is negative
	 */
	private synchronized void initialize(final int a) {
		if (a < 0) {
			throw new IllegalArgumentException("The arity must not be negative");
		}
		arity = a;
		primaryI = new Integer[a];
		Arrays.fill(primaryI, 0);
		if (a > 0) {
			primaryI[0] = 1;
		}
		primary = new TreeSet<ITuple>(new TupleComparator(primaryI));
		indexes.clear();
		indexes.put(Arrays.hashCode(primaryI), primary);
	}

	@Override
	public Iterator<ITuple> iterator() {
		return new UnmodifiableIterator<ITuple>(primary.iterator());
	}

	@Override
	public int size() {
		return primary.size();
	}

	@Override
	public synchronized boolean add(final ITuple t) {
		if (!isEmpty()) { // assert the arity
			if (t.getArity() != arity) {
				throw new IllegalArgumentException(
						"The arity of the tuple must be " + arity + " but was "
								+ t.getArity());
			}
		} else { // initialize the relation
			initialize(t.getArity());
		}

		boolean changed = false;
		if (primary.add(t)) {
			changed = true;
			for (final SortedSet<ITuple> s : indexes.values()) {
				s.add(t);
			}
		}
		return changed;
	}

	@Override
	public synchronized boolean remove(final Object o) {
		boolean changed = false;
		if (primary.remove(o)) {
			changed = true;
			for (final SortedSet<ITuple> s : indexes.values()) {
				s.remove(o);
			}
		}
		return changed;
	}

	public Comparator<? super ITuple> comparator() {
		return primary.comparator();
	}

	public ITuple first() {
		return primary.first();
	}

	public SortedSet<ITuple> headSet(ITuple toElement) {
		return Collections.unmodifiableSortedSet(primary.headSet(toElement));
	}

	public ITuple last() {
		return primary.last();
	}

	public SortedSet<ITuple> subSet(ITuple fromElement, ITuple toElement) {
		return Collections.unmodifiableSortedSet(primary.subSet(fromElement,
				toElement));
	}

	public SortedSet<ITuple> tailSet(ITuple fromElement) {
		return Collections.unmodifiableSortedSet(primary.tailSet(fromElement));
	}

	/**
	 * <p>
	 * Returns a sorted set which is sorted on the specified indexes.
	 * </p>
	 * <p>
	 * The indedexes are specified as follows: All indexes you don't want to
	 * sort on are 0, the index you want to sort on mainly is 1, the second
	 * index (which will be taken into account if the terms at the given
	 * possition of the previous index are equal) is 2, and so on.
	 * </p>
	 * <p>
	 * e.g.: If you want a set of tuples first sorted on the third and then
	 * sorted on the first term you give an index of <code>[2,0,1]</code>.
	 * </p>
	 * 
	 * @param idx
	 *            the index on which should be sorted
	 * @return an <b>unmodifiable</b> set sorted on the given indexes
	 * @throws NullPointerException
	 *             if the indexes is {@code null}
	 * @throws IllegalArgumentException
	 *             if the length of the indexes doesn't match the arity of the
	 *             relation
	 */
	public SortedSet<ITuple> indexOn(final Integer[] idx) {
		if (idx == null) {
			throw new NullPointerException("The index must not be null");
		}
		if (idx.length != arity) {
			throw new IllegalArgumentException("The indexlength " + idx.length
					+ " must match the arity of the relation " + arity);
		}

		SortedSet<ITuple> s = indexes.get(Arrays.hashCode(idx));
		if (s == null) { // the index wasn't created yet
			s = createIndex(idx);
		}
		return Collections.unmodifiableSortedSet(s);
	}

	/**
	 * <p>
	 * Creates and adds a new index to the index map.
	 * </p>
	 * <p>
	 * How to construct the indexes, see {@link #indexOn(Integer[])}
	 * </p>
	 * 
	 * @param idx
	 *            the indexes to sort on
	 * @return the newly created sorted set.
	 * @throws NullPointerException
	 *             if the indexes is {@code null}
	 * @throws IllegalArgumentException
	 *             if the length of the indexes doesn't match the arity of the
	 *             relation
	 * @see #indexOn(Integer[])
	 */
	private synchronized SortedSet<ITuple> createIndex(final Integer[] idx) {
		if (idx == null) {
			throw new NullPointerException("The index must not be null");
		}
		if (idx.length != arity) {
			throw new IllegalArgumentException("The indexlength " + idx.length
					+ " must match the arity of the relation " + arity);
		}

		final SortedSet<ITuple> s = new TreeSet<ITuple>(
				new TupleComparator(idx));
		s.addAll(primary);
		indexes.put(Arrays.hashCode(idx), s);
		return s;
	}

	public int getArity() {
		return arity;
	}

	/**
	 * <p>
	 * Compares two tuples according to a given set of indexes.
	 * </p>
	 * <p>
	 * $Id: IndexingOnTheFlyRelation.java,v 1.1 2007-02-15 08:42:37 poettler_ric Exp $
	 * </p>
	 * 
	 * @author Richard Pöttler, richard dot poettler at deri dot org
	 * @version $Revision: 1.1 $
	 */
	private static class TupleComparator implements Comparator<ITuple> {

		/**
		 * Array holding the position and the priority of the indexes on which
		 * to sort.
		 */
		private Integer[] indexOrder;

		/**
		 * <p>
		 * Creates a new comparator which sorts on a given set of positions in
		 * the tuple
		 * </p>
		 * <p>
		 * The indedexes are specified as follows: All indexes you don't want to
		 * sort on are 0, the index you want to sort on mainly is 1, the second
		 * index (which will be taken into account if the terms at the given
		 * possition of the previous index are equal) is 2, and so on.
		 * </p>
		 * <p>
		 * e.g.: If you want a set of tuples first sorted on the third and then
		 * sorted on the first term you give an index of <code>[2,0,1]</code>.
		 * </p>
		 * 
		 * @param indexes
		 *            the indexes on which to sort the tuples
		 */
		public TupleComparator(final Integer[] indexes) {
			if (indexes == null) {
				throw new NullPointerException("The indexes must not be null");
			}

			// TODO: maybe a defensive copy should be made while computation
			final List<Integer> order = new ArrayList<Integer>(indexes.length);
			final List<Integer> idx = Arrays.asList(indexes);
			if (indexes.length > 0) {
				// constructing the order on which the columns will be comparedc
				int pos = 0;
				for (int i = Math.max(1, Collections.min(idx)), max = Collections
						.max(idx); i <= max; i++) {
					if ((pos = idx.indexOf(i)) > -1) {
						order.add(pos);
					}
				}
				for (int i = 0, max = indexes.length; i < max; i++) {
					if (!order.contains(i)) {
						order.add(i);
					}
				}
			}
			indexOrder = new Integer[order.size()];
			indexOrder = order.toArray(indexOrder);
		}

		public int compare(ITuple o1, ITuple o2) {
			if ((o1 == null) || (o2 == null)) {
				throw new NullPointerException("The tuples must not be null");
			}

			int res = 0;
			for (final int i : indexOrder) {
				if ((res = o1.getTerm(i).compareTo(o2.getTerm(i))) != 0) {
					return res;
				}
			}
			return 0;
		}
	}

	/**
	 * <p>
	 * This is a Iterator to mask another Iterator as immutable. More precisely
	 * it throws an exception when the {@code remove} method is invoked and
	 * passes all other calls to the inner iterator..
	 * </p>
	 * <p>
	 * $Id: IndexingOnTheFlyRelation.java,v 1.1 2007-02-15 08:42:37 poettler_ric Exp $
	 * </p>
	 * 
	 * @author richi
	 * @version $Revision: 1.1 $
	 * 
	 * @param <Type>
	 */
	private static class UnmodifiableIterator<Type> implements Iterator<Type> {

		/** Holds the origianl iterator. */
		private final Iterator<Type> i;

		/**
		 * Creates a new immutable iterator.
		 * 
		 * @param it
		 *            the iterator to save from modifications
		 * @throws NullPointerException
		 *             if the iterator is {@code null}
		 */
		public UnmodifiableIterator(final Iterator<Type> it) {
			if (it == null) {
				throw new NullPointerException("The iterator must not be null");
			}
			i = it;
		}

		public boolean hasNext() {
			return i.hasNext();
		}

		public Type next() {
			return i.next();
		}

		/**
		 * <p>
		 * Throws always an UnsupportedOperationException, bacause this iterator
		 * is immutable
		 * </p>
		 * 
		 * @throws UnsupportedOperationException
		 *             because this iterator is immutable
		 */
		public void remove() {
			throw new UnsupportedOperationException("This iterator is imutable");
		}
	}
}
