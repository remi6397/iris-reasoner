package org.deri.iris.terms.concrete;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IList;

public class List implements IList {

	private final java.util.List<IConcreteTerm> items;
	
	public List() {
		items = new ArrayList<IConcreteTerm>();
	}
	
	@Override
	public URI getDatatypeIRI() {
		return URI.create(IList.DATATYPE_URI);
	}

	@Override
	public String toCanonicalString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		for (IConcreteTerm item : items) {
			builder.append(item.toCanonicalString());
		}
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	public Object getValue() {
		return new ArrayList<IConcreteTerm>(items);
	}

	@Override
	public boolean isGround() {
		// TODO if variables are supported then check for variables.
		
		return true;
	}

	@Override
	public int compareTo(ITerm otherTerm) {
		if (!(otherTerm instanceof IList)) {
			return 1;
		}
		
		IList otherList = (IList) otherTerm;
		
		if (size() != otherList.size())
			return 1;
		
		for (int i = 0; i < items.size(); i++) {
			if (!(this.get(i).equals(otherList.get(i))))
				return this.get(i).compareTo(otherList.get(i));
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof IList)) {
			return false;
		}
		
		IList otherList = (IList) arg0;
		
		if (size() != otherList.size())
			return false;
		
		for (int i = 0; i < items.size(); i++) {
			if (!(this.get(i).equals(otherList.get(i))))
				return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return items.hashCode();
	}

	@Override
	public boolean add(IConcreteTerm element) {
		return items.add(element);
	}

	@Override
	public void add(int index, IConcreteTerm element) {
		items.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends IConcreteTerm> c) {
		return items.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends IConcreteTerm> c) {
		return items.addAll(index, c);
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public boolean contains(Object o) {
		return items.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IConcreteTerm get(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<IConcreteTerm> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<IConcreteTerm> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<IConcreteTerm> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IConcreteTerm remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IConcreteTerm set(int arg0, IConcreteTerm arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public java.util.List<IConcreteTerm> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
