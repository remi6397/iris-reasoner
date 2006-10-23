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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IComparator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.tuple.BasicComparator;


/**
 * This is a simple Relation implementation based on a TreeSet, so no dublicates
 * are allowed.<br/><br/><b>This implementaion is thread-save.</b>
 * 
 * @author richi
 * @author Darko Anicic, DERI Innsbruck
 * 
 */
public class Relation implements IRelation {

	/** The Comparator to compare all tuples to each other */
	private Comparator comparator;

	/** The SortedSet containing all the elements */
	private SortedSet<ITuple> elements;

	private int arity;

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
		this.comparator = new BasicComparator(arity);
		this.elements = new TreeSet<ITuple>(this.comparator);
		this.arity = arity;
		WRITE.unlock();
	}

	/**
	 * @param comp
	 * 					 -	Comparator that defines tuple ordering 
	 * 						in the relation.
	 */
	public Relation(IComparator comp) {
		WRITE.lock();
		this.comparator = comp;
		this.elements = new TreeSet<ITuple>(comparator);
		this.arity = comp.getArity();
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
			return this.comparator;
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

	public int getArity() {
		return this.arity;
	}
}
