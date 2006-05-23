package org.deri.iris.storage;

/*
 * with aspectJ the code scattering for the locks could be prevented and the 
 * code would be much less error-phrone and more readable
 * 
 * TODO: write a test for this relation
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.Relation;
import org.deri.iris.api.terms.ITerm;

/**
 * This is a Relation implementation which sorts on every column (term) of of
 * the stored tuples. Therefor it creates an index for every column and holds
 * it. The tuples will be indexed every time you add or remove a tuple to or
 * from this relation. Substitutional to that the relation wont have to be
 * sorted if you join on a column, because it is already sorted.</br>
 * <b>This implementaion is thread-save.</b>
 * 
 * @author richi
 * 
 */
public class AlwaysIndexedRelation implements Relation<ITuple> {

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
	private List<Map<ITerm, Set<ITuple>>> indexlist;

	/** The overall lock for this relation. */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The first index of the indexlist */
	private Map<ITerm, Set<ITuple>> primaryIndex;

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
		primaryIndex = indexlist.get(0);
		WRITE.unlock();
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
					elements.add(t);
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
				ITuple t = (ITuple) o;
				if (t.getArity() == arity) {
					for (int iCounter = 0; iCounter < arity; iCounter++) {
						if (indexlist.get(iCounter).get(t.getTerm(iCounter))
								.remove(t)) {
							changed = true;
						}
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
		indexlist = new ArrayList<Map<ITerm, Set<ITuple>>>(columns);
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
}
