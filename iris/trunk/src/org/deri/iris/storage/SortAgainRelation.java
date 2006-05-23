package org.deri.iris.storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.Relation;

/**
 * This is a simple Relation implementation based on a TreeSet, so no dublicates
 * are allowed.
 * </br> <b>ATTENTION:</b> Everytime the index with the
 * sortOn(int) method is changed the whole set will be sorted agein, which is
 * very time consuming.</br></br>
 * <b>This implementaion is thread-save.</b>
 * 
 * @author richi
 * 
 */
public class SortAgainRelation implements Relation<ITuple> {

	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The read lock */
	private final Lock READ = LOCK.readLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();

	/** The Set containing all the elements */
	private Set<ITuple> elements;

	/** The Comparator to compare all tuples to each other */
	private TupleComparator comparator;

	public SortAgainRelation() {
		WRITE.lock();
		comparator = new TupleComparator();
		elements = new TreeSet<ITuple>(comparator);
		WRITE.unlock();
	}

	/**
	 * Reorganizes the Tree, so that the Tree is sorted on the <i>index</i>'th
	 * Term.</br></br>ATTENTION: changing the index is very time consuming.
	 * 
	 * @param index
	 *            of the Term on which to sort
	 * @return whether the order of the Tree has changed
	 */
	public boolean sortOn(final int index) {
		WRITE.lock();
		try {
			boolean changed = false;
			if (comparator.getIndexField() != index) {
				comparator.setIndexField(index);
				Set<ITuple> temp = new TreeSet<ITuple>();
				temp.addAll(elements);
				elements = temp;
				changed = true;
			}
			return changed;
		} finally {
			WRITE.unlock();
		}
	}

	public int size() {
		READ.lock();
		try {
			return elements.size();
		} finally {
			READ.unlock();
		}
	}

	public boolean isEmpty() {
		READ.lock();
		try {
			return elements.isEmpty();
		} finally {
			READ.unlock();
		}
	}

	public boolean contains(Object o) {
		READ.lock();
		try {
			return elements.contains(o);
		} finally {
			READ.unlock();
		}
	}

	public Iterator<ITuple> iterator() {
		READ.lock();
		try {
			return elements.iterator();
		} finally {
			READ.unlock();
		}
	}

	public Object[] toArray() {
		READ.lock();
		try {
			return elements.toArray();
		} finally {
			READ.unlock();
		}
	}

	public <T> T[] toArray(T[] a) {
		READ.lock();
		try {
			return elements.toArray(a);
		} finally {
			READ.unlock();
		}
	}

	public boolean add(ITuple o) {
		WRITE.lock();
		try {
			return elements.add(o);
		} finally {
			WRITE.unlock();
		}
	}

	public boolean remove(Object o) {
		WRITE.lock();
		try {
			return elements.remove(o);
		} finally {
			WRITE.unlock();
		}
	}

	public boolean containsAll(Collection<?> c) {
		READ.lock();
		try {
			return elements.containsAll(c);
		} finally {
			READ.unlock();
		}
	}

	public boolean addAll(Collection<? extends ITuple> c) {
		WRITE.lock();
		try {
			return elements.addAll(c);
		} finally {
			WRITE.unlock();
		}
	}

	public boolean removeAll(Collection<?> c) {
		WRITE.lock();
		try {
			return elements.removeAll(c);
		} finally {
			WRITE.unlock();
		}
	}

	public boolean retainAll(Collection<?> c) {
		WRITE.lock();
		try {
			return elements.retainAll(c);
		} finally {
			WRITE.unlock();
		}
	}

	public void clear() {
		WRITE.lock();
		elements.clear();
		WRITE.unlock();
	}

	private static class TupleComparator implements Comparator<ITuple> {

		private int indexField;

		public TupleComparator() {
			this(0);
		}

		public TupleComparator(final int indexField) {
			this.indexField = indexField;
		}

		public void setIndexField(final int indexField) {
			this.indexField = indexField;
		}

		public int getIndexField() {
			return this.indexField;
		}

		@SuppressWarnings("unchecked")
		public int compare(ITuple o1, ITuple o2) {
			return o1.getTerm(indexField).compareTo(o2.getTerm(indexField));
		}
	}
}
