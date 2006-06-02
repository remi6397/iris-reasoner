package org.deri.iris.storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.tuple.TupleComparator;


/**
 * This is a simple Relation implementation based on a TreeSet, so no dublicates
 * are allowed.</br></br><b>This implementaion is thread-save.</b>
 * 
 * @author richi
 * @author Darko Anicic, DERI Innsbruck
 * 
 */
public class Relation implements IRelation<ITuple> {

	/** The Comparator to compare all tuples to each other */
	private Comparator comparator;

	/** The SortedSet containing all the elements */
	private SortedSet<ITuple> elements;

	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The read lock */
	private final Lock READ = LOCK.readLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();

	/**
	 * @param arity
	 * 				- arity of tuples to be stored in the relation.
	 */
	public Relation(int arity) {
		WRITE.lock();
		this.comparator = new TupleComparator(arity);
		this.elements = new TreeSet<ITuple>(comparator);
		
		WRITE.unlock();
	}

	/**
	 * @param comparator
	 * 					 -	Comparator that defines tuple ordering 
	 * 						in the relation.
	 */
	public Relation(Comparator comparator) {
		WRITE.lock();
		this.comparator = comparator;
		this.elements = new TreeSet<ITuple>(comparator);
		WRITE.unlock();
	}
	
	public boolean add(ITuple o) {
		WRITE.lock();
		try {
			return elements.add(o);
		} finally {
			WRITE.unlock();
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

	public void clear() {
		WRITE.lock();
		elements.clear();
		WRITE.unlock();
	}

	public Comparator<? super ITuple> comparator() {
		READ.lock();
		try {
			return comparator;
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

	public boolean containsAll(Collection<?> c) {
		READ.lock();
		try {
			return elements.containsAll(c);
		} finally {
			READ.unlock();
		}
	}

	public ITuple first() {
		READ.lock();
		try {
			return elements.first();
		} finally {
			READ.unlock();
		}
	}

	/**
	 * @param toElement -
	 *            high endpoint (exclusive) of the headSet.
	 * @return Returns a view of the portion of this sorted set whose elements
	 *         are strictly less than toElement.
	 */
	public SortedSet<ITuple> headSet(ITuple toElement) {
		READ.lock();
		try {
			return elements.headSet(toElement);
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

	public Iterator<ITuple> iterator() {
		READ.lock();
		try {
			return elements.iterator();
		} finally {
			READ.unlock();
		}
	}

	public ITuple last() {
		READ.lock();
		try {
			return elements.last();
		} finally {
			READ.unlock();
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

	public int size() {
		READ.lock();
		try {
			return elements.size();
		} finally {
			READ.unlock();
		}
	}
	
	/**
	 * @param fromElement -
	 *            low endpoint (inclusive) of the subSet.
	 * @param toElement -
	 *            high endpoint (exclusive) of the subSet.
	 * @return Returns a view of the portion of this sorted set whose elements
	 *         range from fromElement, inclusive, to toElement, exclusive.
	 */
	public SortedSet<ITuple> subSet(ITuple fromElement, ITuple toElement) {
		READ.lock();
		try {
			return elements.subSet(fromElement, toElement);
		} finally {
			READ.unlock();
		}
	}

	/**
	 * @param fromElement
	 *            The element to position the tree at.
	 * @return Returns a view of the portion of this sorted set whose elements
	 *         are greater than or equal to fromElement.
	 */
	public SortedSet<ITuple> tailSet(ITuple fromElement) {
		READ.lock();
		try {
			return elements.tailSet(fromElement);
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
}
