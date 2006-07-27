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

package org.deri.iris.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 12:07:31
 * 
 * Revision 1.1  21.07.2006 12:01:56  Darko Anicic, DERI Innsbruck
 */
public class Tuple implements ITuple<ITuple>{

	private final int arity;
	
	private List<ITerm> terms = null;
	
	/**
	 * Tuples t0 and t1 are duplicates if they have identical terms  
	 * for each sort index. Used in operations on relations 
	 * (e.g. for tuple sorting – IndexComparator) to increase the 
	 * efficiency of the operations.
	 */
	private ITuple duplicate = null;
	
	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();
	
	
	/**
	 * Creates a tuple defined by the list of terms.
	 * 
	 * @param terms
	 * 				list of terms that create a tuple
	 */
	Tuple(List<ITerm> terms){
		if (terms == null) {
			throw new IllegalArgumentException("Input argument must not be null");
		}
		this.arity = terms.size();
		this.terms = terms;
	}
	
	/**
	 * Creates an empty tuple with an arity defined by the parameter.
	 * 
	 * @param arity
	 * 				arity of the tuple
	 */
	Tuple(int arity){
		if (arity <= 0) {
			throw new IllegalArgumentException("The argument must be " +
					"a positive number greater than 0.");
		}
		this.arity = arity;
		this.terms = new ArrayList<ITerm>(arity);
	}
	
	public int getArity() {
		return this.terms.size();
	}

	public ITerm getTerm(int arg) {
		return this.terms.get(arg);
	}

	public List<ITerm> getTerms() {
		return this.terms;
	}

	public boolean setTerm(int index, ITerm term) {
		if (index >= this.arity) {
			throw new IndexOutOfBoundsException(
					"Can't set a tuple at position: " +
					index + " (the tuple arity is: " + 
					this.terms.size() + ")");
		}
		WRITE.lock();
		try {
			if(index >= this.terms.size()){
				this.terms.add(index, term);
			}else{
				this.terms.set(index, term);
			}
		} finally {
			WRITE.unlock();
		}
		return true;
	}

	public boolean setTerms(List<ITerm> terms) {
		if (terms.size() > this.arity) {
			throw new IndexOutOfBoundsException(
					"Size of the term list: " +
					terms.size() + " is longer than the tuple arity: " + 
					this.arity + ")");
		}
		for(int i=0; i<terms.size(); i++){
			setTerm(i, terms.get(i));
		}
		
		return true;
	}
	
	public boolean setTerms(int index, List<ITerm> terms) {
		if (index + terms.size() > this.arity) {
			throw new IndexOutOfBoundsException(
					"Cannot store the entire list of terms from position: " +
					index + ".");
		}
		for(int i=0; i<terms.size(); i++){
			this.setTerm(index + i, terms.get(i));
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isGround() {
		for(ITerm t : this.terms){
			if(!t.isGround()) return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public String toString(){
		String s = "(";
		for(int i=0; i<this.getArity(); i++){
			s = s + this.getTerm(i).toString();
			if(i<this.getArity()-1)s = s + ", ";
			else s = s + ")";
		}
		if(this.getArity() == 0)s = s + ")";
		return s;
	}

	public void setDuplicate(ITuple duplicate) {
		this.duplicate = duplicate;
	}
	
	public ITuple getDuplicate() {
		return this.duplicate;
	}
	
	@SuppressWarnings("unchecked")
	public int compareTo(ITuple t) {
		if (t == null) {
			throw new IllegalArgumentException("Cannot compare with null");
		}
		int res = 0;
		int minsize = 0;
		
		minsize = Math.min(this.terms.size(), t.getTerms().size());
		for (int iCounter = 0; iCounter < minsize; iCounter++) {
			if ((res = this.terms.get(iCounter).compareTo(t.getTerm(iCounter))) != 0) {
				return res;
			}
		}
		return this.terms.size() - t.getTerms().size();
	}

	@SuppressWarnings("unchecked")
	public int hashCode() {
		int result = 17;
		for (ITerm t : terms) {
			if (t != null) {
				result = result * 37 + t.hashCode();
			}
		}
		return result;
	}
	
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Tuple)) {
			return false;
		}
		Tuple tuple = (Tuple) o;
		ITerm t0, t1;
		for(int i=0; i<tuple.getTerms().size(); i++){
			t0 = this.terms.get(i);
			t1 = tuple.getTerm(i);
			// Correct it!
			if (!(t0.getClass() == t1.getClass())) {
				return false;
			}
			if(t0.compareTo(t1) != 0)
				return false;
		}
		if((this.terms.size() - tuple.getTerms().size()) != 0) return false;
		return true;
	}
}
