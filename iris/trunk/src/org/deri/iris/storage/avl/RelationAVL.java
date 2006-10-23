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

package org.deri.iris.storage.avl;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;

/**
 * NOT IMPLEMENTED! 
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   23.06.2006 17:44:48
 */
public class RelationAVL implements IRelation{

	public Comparator<? super ITuple> comparator() {
		
		return null;
	}

	public SortedSet<ITuple> subSet(ITuple fromElement, ITuple toElement) {
		
		return null;
	}

	public SortedSet<ITuple> headSet(ITuple toElement) {
		
		return null;
	}

	public SortedSet<ITuple> tailSet(ITuple fromElement) {
		
		return null;
	}

	public ITuple first() {
		
		return null;
	}

	public ITuple last() {
		
		return null;
	}

	public int size() {
		
		return 0;
	}

	public boolean isEmpty() {
		
		return false;
	}

	public boolean contains(Object o) {
		
		return false;
	}

	public Iterator<ITuple> iterator() {
		
		return null;
	}

	public Object[] toArray() {
		
		return null;
	}

	public <T> T[] toArray(T[] a) {
		
		return null;
	}

	public boolean add(ITuple o) {
		
		return false;
	}

	public boolean remove(Object o) {
		
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		
		return false;
	}

	public boolean addAll(Collection<? extends ITuple> c) {
		
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		
		return false;
	}

	public void clear() {
		
		
	}

	public int getArity() {
		// TODO Auto-generated method stub
		return 0;
	}

}
