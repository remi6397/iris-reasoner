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

/*
 * with aspectJ the code scattering for the locks could be prevented and the 
 * code would be much less error-phrone and more readable
 * 
 * TODO: write a test for this relation
 * TODO: clone the tuples used to limit the subsets
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * This is a Relation implementation which sorts on every column (term) of of
 * the stored tuples. Therefor it creates an index for every column and holds
 * it. The tuples will be indexed every time you add or remove a tuple to or
 * from this relation. Substitutional to that the relation wont have to be
 * sorted if you join on a column, because it is already sorted.<br/> <b>This
 * implementaion is thread-save.</b>
 * 
 * @author richi
 * 
 */
public class AlwaysIndexedRelation implements IRelation<ITuple> {

	/**
	 * The initial amount of columns for this relation. Will be used to specify
	 * the initial size for the indexlist.
	 */
	private static final int INITIAL_COLS = 5;

	/** The initial amount of columns for this relation. NOT USED AT THE MOMENT. */
	private static final int INITIAL_ROWS = 10;

	/** How many columns are stored in this relation. */
	private int arity;

	/**
	 * A list containing all indexes. An index is a Map sorted on a special
	 * column (term) of the tuple. It uses the Term at this special position as
	 * key an the value will be a set of the tuples with the key at the position
	 * this index sorts on. indexlist[0] will sort on the first column,
	 * indexlist[1] on the second, and so on.
	 */
	private List<SortedMap<ITerm, Set<ITuple>>> indexlist;

	/** The overall lock for this relation. */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The first index of the indexlist */
	private SortedMap<ITerm, Set<ITuple>> primaryIndex;

	/** The read lock for this relation. */
	private final Lock READ = LOCK.readLock();

	/** The write lock for this relation. */
	private final Lock WRITE = LOCK.writeLock();

	public AlwaysIndexedRelation() {
		WRITE.lock();
		reset(INITIAL_ROWS, INITIAL_COLS);
		WRITE.unlock();
	}

	public boolean add(ITuple tuple) {
		WRITE.lock();
		try {
			boolean changed = false;
			if (isEmpty()) { // if its empty -> initialize the indexes
				initializeForTuple(tuple);
			} else if (arity == tuple.getArity()) { // validate arity of the
				// tuple
				throw new IllegalArgumentException(
						"Tried to add a tuple of arity "
								+ tuple.getArity()
								+ " to a relation which was initialized for arity "
								+ arity);
			}

			if (searchForTuple(tuple) == null) { // check whether tuple
				// already exists -> update the indexes
				for (int iCounter = 0; iCounter < tuple.getArity(); iCounter++) {
					ITerm indexTerm = tuple.getTerm(iCounter);

					Set<ITuple> indexed = indexlist.get(iCounter)
							.get(indexTerm);
					if (indexed == null) {
						indexed = new TreeSet<ITuple>();
					}
					indexed.add(tuple);
					indexlist.get(iCounter).put(indexTerm, indexed);
				}
				changed = true;
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	public boolean addAll(Collection<? extends ITuple> c) {
		WRITE.lock();
		try {
			boolean changed = false;
			for (ITuple t : c) {
				if (add(t)) {
					changed = true;
				}
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	public void clear() {
		WRITE.lock();
		reset(INITIAL_ROWS, indexlist.size());
		WRITE.unlock();
	}

	public boolean contains(Object o) {
		READ.lock();
		try {
			return primaryIndex.containsValue(o);
		} finally {
			READ.unlock();
		}
	}

	public boolean containsAll(Collection<?> c) {
		READ.lock();
		try {
			for (Object o : c) {
				if (!contains(o)) {
					return false;
				}
			}
			return true;
		} finally {
			READ.unlock();
		}
	}

	/**
	 * Sets the arity of this Relation and initializes the indexList, so that
	 * every index has an empty map, to prevent NullPointerExceptions.
	 * 
	 * @param tuple
	 *            for which to initialize this Relaion
	 */
	protected void initializeForTuple(final ITuple tuple) {
		WRITE.lock();
		arity = tuple.getArity();
		for (int iCounter = 0; iCounter < arity; iCounter++) {
			indexlist.set(iCounter, new TreeMap<ITerm, Set<ITuple>>());
		}
		sortOn(0);
		WRITE.unlock();
	}

	/**
	 * Tells the relation to use another index
	 * 
	 * @param index
	 *            of the Term on which to sort
	 * @return whether the order of the Tree has changed
	 */
	public boolean sortOn(final int index) {
		WRITE.lock();
		try {
			primaryIndex = indexlist.get(index);
			return false;
		} finally {
			WRITE.unlock();
		}
	}

	public boolean isEmpty() {
		READ.lock();
		try {
			return (size() == 0);
		} finally {
			READ.unlock();
		}
	}

	public Iterator<ITuple> iterator() {
		READ.lock();
		try {
			return Collections.unmodifiableCollection(listAllTuples())
					.iterator();
		} finally {
			READ.unlock();
		}
	}

	/**
	 * Creates a Collection of all the tuples stored in this relation.
	 * 
	 * @return the Collection of all the tuples.
	 */
	protected Collection<ITuple> listAllTuples() {
		// TODO: think about caching this list!
		READ.lock();
		try {
			final List<ITuple> elements = new ArrayList<ITuple>();
			for (Set<ITuple> s : primaryIndex.values()) {
				for (ITuple t : s) {
					elements.add(elements.size(), t);
				}
			}
			return elements;
		} finally {
			READ.unlock();
		}
	}

	public boolean remove(Object o) {
		WRITE.lock();
		try {
			boolean changed = false;
			if (!(o instanceof ITuple)) {
				return false;
			}
			ITuple t = (ITuple) o;
			if (t.getArity() == arity) {
				for (int iCounter = 0; iCounter < arity; iCounter++) {
					SortedMap<ITerm, Set<ITuple>> indexMap = indexlist
							.get(iCounter);
					ITerm term = t.getTerm(iCounter);
					if (indexMap.get(term).remove(t)) {
						if (indexMap.get(term).isEmpty()) {
							indexMap.remove(term);
						}
						changed = true;
					}
				}
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	public boolean removeAll(Collection<?> c) {
		WRITE.lock();
		try {
			boolean changed = false;
			for (Object o : c) {
				if (remove(o)) {
					changed = true;
				}
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	/**
	 * Reinitializes the object.
	 * 
	 * @param rows
	 *            the initial size of this collection
	 * @param columns
	 *            the arrity of the tuples stored in here
	 */
	protected void reset(final int rows, final int columns) {
		WRITE.lock();
		indexlist = new ArrayList<SortedMap<ITerm, Set<ITuple>>>(columns);
		arity = 0;
		WRITE.unlock();
	}

	public boolean retainAll(Collection<?> c) {
		WRITE.lock();
		try {
			boolean changed = false;
			for (Object o : toArray()) {
				if (!c.contains(o)) {
					if (remove(o)) {
						changed = true;
					}
				}
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	/**
	 * Searches through this Collection for a tuple equal to the submitted one.
	 * If a tuple was found a reference to this tuble will be returned,
	 * otherwise null.
	 * 
	 * @param tuple
	 *            the tuple to search for.
	 * @return a reference to the found tuple of null.
	 */
	protected ITuple searchForTuple(final ITuple tuple) {
		READ.lock();
		try {
			Set<ITuple> tupleSet = primaryIndex.get(tuple.getTerm(0));
			ITuple[] indexed = new ITuple[tupleSet.size()];
			indexed = tupleSet.toArray(indexed);
			for (ITuple t : indexed) {
				if (t.equals(tuple)) {
					return t;
				}
			}
			return null;
		} finally {
			READ.unlock();
		}
	}

	public int size() {
		READ.lock();
		try {
			return listAllTuples().size();
		} finally {
			READ.unlock();
		}
	}

	public Object[] toArray() {
		READ.lock();
		try {
			return listAllTuples().toArray();
		} finally {
			READ.unlock();
		}
	}

	public <T> T[] toArray(T[] a) {
		READ.lock();
		try {
			return listAllTuples().toArray(a);
		} finally {
			READ.unlock();
		}
	}

	public Comparator<? super ITuple> comparator() {
		return null;
	}

	/*
	 * depends on which the index should be taken, by default primaryIndex is
	 * taken
	 */
	public SortedSet<ITuple> subSet(ITuple fromElement, ITuple toElement) {
		WRITE.lock();
		try {
			return new SubRelation(fromElement, toElement, 0);
		} finally {
			WRITE.unlock();
		}
	}

	/*
	 * depends on which the index should be taken, by default primaryIndex is
	 * taken
	 */
	public SortedSet<ITuple> headSet(ITuple toElement) {
		WRITE.lock();
		try {
			return new SubRelation(null, toElement, 0);
		} finally {
			WRITE.unlock();
		}
	}

	/*
	 * depends on which the index should be taken, by default primaryIndex is
	 * taken
	 */
	public SortedSet<ITuple> tailSet(ITuple fromElement) {
		WRITE.lock();
		try {
			return new SubRelation(fromElement, null, 0);
		} finally {
			WRITE.unlock();
		}
	}

	/*
	 * depends on which the index should be taken, by default primaryIndex is
	 * taken
	 */
	public ITuple first() {
		READ.lock();
		try {
			return primaryIndex.get(primaryIndex.firstKey()).iterator().next();
		} finally {
			READ.unlock();
		}
	}

	/*
	 * depends on which the index should be taken, by default primaryIndex is
	 * taken
	 */
	public ITuple last() {
		READ.lock();
		try {
			return primaryIndex.get(primaryIndex.lastKey()).iterator().next();
		} finally {
			READ.unlock();
		}
	}

	/**
	 * This SubRelation represents a limited view to the underlying
	 * AlwaysIndedexedRelation. Workin on such a SubRelation should be hold to a
	 * minimum, because every tuple got to be checkted whether it is shown,
	 * added or counted.
	 * 
	 * @author richi
	 * 
	 */
	private class SubRelation implements SortedSet<ITuple> {

		private ITuple lowest = null;

		private ITuple highest = null;

		private int index = 0;

		private Map<ITerm, Set<ITuple>> primaryIndex;

		/**
		 * The constructor for this view which takes the lowest (inclusive)
		 * point, the highest (exclusive) point and on which column of the
		 * original view should be sorted. The index must be smaller than the
		 * arity of the original relation and lowest must be smaller than
		 * highest if they are sorted on the term at the position index points
		 * to, otherwise a exception will be thrown.
		 * 
		 * @param lowest
		 *            the lowest point of this view, or null, if there isn't any
		 * @param highest
		 *            the highest point of this view, or null, if there isn't
		 *            any
		 * @param index
		 *            on which position should be sorted.
		 * @throws IllegalArgumentException
		 *             if the aritys of the tuples doesn't match, the index is
		 *             bigger or equal than the arity, of highest is bigger than
		 *             lowest.
		 */
		@SuppressWarnings("unchecked")
		public SubRelation(final ITuple lowest, final ITuple highest,
				final int index) {
			WRITE.lock();

			if (index >= arity) {
				throw new IllegalArgumentException("The index " + index
						+ " is too big for arity " + arity);
			}

			if (((lowest != null) && (lowest.getArity() != arity))
					|| ((highest != null) && (highest.getArity() != arity))) {
				throw new IllegalArgumentException(
						"The arity of the tuple doesn't match the"
								+ " arity of the relation: " + arity);
			}

			if (lowest.getTerm(index).compareTo(highest.getTerm(index)) > 0) {
				throw new IllegalArgumentException("lowest (" + lowest
						+ ") is higher than highest (" + highest + ").");
			}

			this.lowest = lowest;
			this.highest = highest;
			this.index = index;
			this.primaryIndex = indexlist.get(index);
			WRITE.unlock();
		}

		public Comparator<? super ITuple> comparator() {
			return null;
		}

		public SortedSet<ITuple> subSet(ITuple fromElement, ITuple toElement) {
			WRITE.lock();
			try {
				return new SubRelation(
						((checkValidRange(fromElement)) ? fromElement : lowest),
						((checkValidRange(toElement)) ? fromElement : highest),
						index);
			} finally {
				WRITE.unlock();
			}
		}

		public SortedSet<ITuple> headSet(final ITuple toElement) {
			WRITE.lock();
			try {
				return new SubRelation(lowest,
						((checkValidRange(toElement)) ? toElement : highest),
						index);
			} finally {
				WRITE.unlock();
			}
		}

		public SortedSet<ITuple> tailSet(ITuple fromElement) {
			WRITE.lock();
			try {
				return new SubRelation(
						((checkValidRange(fromElement)) ? fromElement : lowest),
						highest, index);
			} finally {
				WRITE.unlock();
			}
		}

		public ITuple first() {
			READ.lock();
			try {
				// TODO: optimize if the list is cached
				if (lowest == null) {
					return AlwaysIndexedRelation.this.first();
				} else {
					return primaryIndex.get(getValidIndexes().first())
							.iterator().next();
				}
			} finally {
				READ.unlock();
			}
		}

		public ITuple last() {
			READ.lock();
			try {
				// TODO: optimize if the list is cached
				if (highest == null) {
					return AlwaysIndexedRelation.this.last();
				} else {
					return primaryIndex.get(getValidIndexes().last())
							.iterator().next();
				}
			} finally {
				READ.unlock();
			}
		}

		public int size() {
			READ.lock();
			try {
				return listAllTuples().size();
			} finally {
				READ.unlock();
			}
		}

		public boolean isEmpty() {
			READ.lock();
			try {
				return (size() == 0);
			} finally {
				READ.unlock();
			}
		}

		public boolean contains(Object o) {
			READ.lock();
			try {
				if (checkValidRange(o)) {
					return AlwaysIndexedRelation.this.contains(o);
				}
				return false;
			} finally {
				READ.unlock();
			}
		}

		public Iterator<ITuple> iterator() {
			READ.lock();
			try {
				return listAllTuples().iterator();
			} finally {
				READ.unlock();
			}
		}

		public Object[] toArray() {
			READ.lock();
			try {
				return listAllTuples().toArray();
			} finally {
				READ.unlock();
			}
		}

		public <T> T[] toArray(T[] a) {
			READ.lock();
			try {
				return listAllTuples().toArray(a);
			} finally {
				READ.unlock();
			}
		}

		public boolean add(ITuple o) {
			WRITE.lock();
			try {
				if (checkValidRange(o)) {
					return AlwaysIndexedRelation.this.add(o);
				}
				return false;
			} finally {
				WRITE.unlock();
			}
		}

		public boolean remove(Object o) {
			WRITE.lock();
			try {
				if (checkValidRange(o)) {
					return AlwaysIndexedRelation.this.remove(o);
				}
				return false;
			} finally {
				WRITE.unlock();
			}
		}

		public boolean containsAll(Collection<?> c) {
			READ.lock();
			try {
				for (Object o : c) {
					if (!contains(o)) {
						return false;
					}
				}
				return true;
			} finally {
				READ.unlock();
			}
		}

		public boolean addAll(Collection<? extends ITuple> c) {
			WRITE.lock();
			boolean changed = false;
			try {
				for (ITuple t : c) {
					if (add(t)) {
						changed = true;
					}
				}
				return changed;
			} finally {
				WRITE.unlock();
			}
		}

		public boolean retainAll(Collection<?> c) {
			WRITE.lock();
			boolean changed = false;
			try {
				for (Object o : toArray()) {
					if (!c.contains(o)) {
						if (remove(o)) {
							changed = true;
						}
					}
				}
				return changed;
			} finally {
				WRITE.unlock();
			}
		}

		public boolean removeAll(Collection<?> c) {
			WRITE.lock();
			boolean changed = false;
			try {
				for (Object o : c) {
					if (remove(o)) {
						changed = true;
					}
				}
				return changed;
			} finally {
				WRITE.unlock();
			}
		}

		public void clear() {
			WRITE.lock();
			for (ITerm t : getValidIndexes()) {
				for (ITuple tup : primaryIndex.get(t)) {
					AlwaysIndexedRelation.this.remove(tup);
				}
			}
			WRITE.unlock();
		}

		/**
		 * Checks whether a tuple would be valid to be stored in the relation
		 * depending on the limits of this view. The term at the the indexcolumn
		 * will be checkt about the corresponding term of hithest and lowest.
		 * 
		 * @param o
		 *            the tuple to check
		 * @return whether it is valid or not.
		 * @throws IllegalArgumentException
		 *             if the arities don't match.
		 */
		@SuppressWarnings("unchecked")
		protected boolean checkValidRange(final Object o) {
			READ.lock();
			try {
				if (!(o instanceof ITuple)) {
					return false;
				}
				ITuple t = (ITuple) o;
				if (t.getArity() != arity) {
					throw new IllegalArgumentException(
							"The arity of the tuple doesn't match the "
									+ "arity of the relation: " + arity);
				}
				return checkValidIndexTerm(t.getTerm(index));
			} finally {
				READ.unlock();
			}
		}

		/**
		 * Checks whether the term would be valid index to be stored in this
		 * view.
		 * 
		 * @param t
		 *            the term to check
		 * @return whether the term is valid.
		 */
		@SuppressWarnings("unchecked")
		protected boolean checkValidIndexTerm(final ITerm t) {
			READ.lock();
			try {
				if ((lowest != null)
						&& (t.compareTo(lowest.getTerm(index)) < 0)) {
					return false;
				}
				if ((highest != null)
						&& (t.compareTo(highest.getTerm(index)) >= 0)) {
					return true;
				}
				return true;
			} finally {
				READ.unlock();
			}
		}

		/**
		 * Returns a list of all indexes of this view stored in the underlying
		 * relation
		 * 
		 * @return the list of indexes
		 */
		protected SortedSet<ITerm> getValidIndexes() {
			// TODO: think about caching until the underlying list has changed
			READ.lock();
			try {
				SortedSet<ITerm> indexes = new TreeSet<ITerm>();
				for (ITerm t : primaryIndex.keySet()) {
					if (checkValidIndexTerm(t)) {
						indexes.add(t);
					}
				}
				return indexes;
			} finally {
				READ.unlock();
			}
		}

		/**
		 * Returns a List of all tuples for this view stored in the relation.
		 * 
		 * @return the list of tuples
		 */
		protected Collection<ITuple> listAllTuples() {
			READ.lock();
			try {
				List<ITuple> elements = new ArrayList<ITuple>();
				for (ITerm t : getValidIndexes()) {
					for (ITuple tup : primaryIndex.get(t)) {
						elements.add(elements.size(), tup);
					}
				}
				return elements;
			} finally {
				READ.unlock();
			}
		}
	}

	public int getArity() {
		// TODO Auto-generated method stub
		return 0;
	}
}
