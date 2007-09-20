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
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * A simple tuple implementation. This implementation is thread-safe.
 * </p>
 * <p>
 * $Id: Tuple.java,v 1.17 2007-07-10 09:47:25 poettler_ric Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.17 $
 */
public class Tuple implements ITuple{

	/** The terms stored in this tuple. */
	private final ITerm[] terms;
	
	/** The Lock to make this object threadsafe. */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The write lock. */
	private final Lock WRITE = LOCK.writeLock();

	/** The read lock. */
	private final Lock READ = LOCK.readLock();
	
	
	/**
	 * Creates a tuple defined by the list of terms.
	 * 
	 * @param terms list of terms that create a tuple
	 * @throws NullPointerException if terms is <code>null</code>
	 */
	Tuple(final Collection<ITerm> t){
		if (t == null) {
			throw new NullPointerException("Input argument must not be null");
		}
		terms = t.toArray(new ITerm[t.size()]);
	}
	
	/**
	 * Creates an empty tuple with an given arity.
	 * 
	 * @param arity	arity of the tuple
	 * @throws IllegalArgumentException if the arity is negative
	 */
	Tuple(final int arity){
		if (arity < 0) {
			throw new IllegalArgumentException("The arity must not be negative");
		}
		terms = new ITerm[arity];
		Arrays.fill(terms, null);
	}
	
	public int getArity() {
		READ.lock();
		try {
			return terms.length;
		} finally {
			READ.unlock();
		}
	}

	public ITerm getTerm(final int arg) {
		if (arg >= terms.length) {
			throw new IndexOutOfBoundsException("Term at position " + arg + 
					" was requested from a tuple with arity " + 
					terms.length);
		}
		READ.lock();
		try {
			return terms[arg];
		} finally {
			READ.unlock();
		}
	}

	public List<ITerm> getTerms() {
		READ.lock();
		try {
			return Arrays.asList(terms);
		} finally {
			READ.unlock();
		}
	}

	public ITerm setTerm(final int index, final ITerm term) {
		if (index > terms.length) {
			throw new IndexOutOfBoundsException(
					"Can't set a tuple at position: " +
					index + " (as the arity of the tuple is: " + 
					terms.length + ")");
		}
		WRITE.lock();
		try {
			final ITerm ret = terms[index];
			terms[index] = term;
			return ret;
		} finally {
			WRITE.unlock();
		}
	}

	public void setTerms(final Collection<ITerm> terms) {
		WRITE.lock();
		try {
			setTerms(0, terms);
		} finally {
			WRITE.unlock();
		}
	}
	
	public void setTerms(final int index, final Collection<ITerm> t) {
		if (index + t.size() > terms.length) {
			throw new IndexOutOfBoundsException("Couldn't store " + t.size() + 
					" elements starting from " + index + 
					" in a tuple wiht arity " + terms.length);
		}
		WRITE.lock();
		try {
			int i = index;
			for (final ITerm term : t) {
				terms[i++] = term;
			}
		} finally {
			WRITE.unlock();
		}
	}
	
	public boolean isGround() {
		READ.lock();
		try {
			for(final ITerm t : terms){
				if(!t.isGround()) {
					return false;
				}
			}
		} finally {
			READ.unlock();
		}
		return true;
	}

	public String toString(){
		READ.lock();
		try {
			if (terms.length <= 0) {
				return "()";
			}
			final StringBuilder buffer = new StringBuilder();
			buffer.append("(");
			for (final ITerm t : terms) {
				buffer.append(t).append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length()).append(")");
			return buffer.toString();
		} finally {
			READ.unlock();
		}
	}

	public int compareTo(ITuple t) {
		if (t == null) {
			throw new NullPointerException("Cannot compare with null");
		}
		
		READ.lock();
		try {
			int res = 0;
			for (int i = 0; i < Math.min(terms.length, t.getTerms().size()); i++) {
				if ((res = terms[i].compareTo(t.getTerm(i))) != 0) {
					return res;
				}
			}
			return terms.length - t.getTerms().size();
		} finally {
			READ.unlock();
		}
	}

	public int hashCode() {
		READ.lock();
		try {
			return Arrays.hashCode(terms);
		} finally {
			READ.unlock();
		}
	}
	
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Tuple)) {
			return false;
		}
		READ.lock();
		try {
			final Tuple tuple = (Tuple) o;
			if(terms.length != tuple.getTerms().size()) {
				return false;
			}
			for(int i=0; i<tuple.getTerms().size(); i++){
				final ITerm t0 = terms[i];
				final ITerm t1 = tuple.getTerm(i);
				if ((t0 != t1) && !t0.equals(t1)) {
					return false;
				}
			}
			return true;
		} finally {
			READ.unlock();
		}
	}

	public Set<IVariable> getVariables() {
		READ.lock();
		try {
			Set<IVariable> variables = new HashSet<IVariable>();
			for(ITerm term : this.terms){
				if(term instanceof IVariable)
					variables.add((IVariable)term);
				if(term instanceof IConstructedTerm)
					variables.addAll(getVariables((IConstructedTerm)term));
			}
			return variables;
		} finally {
			READ.unlock();
		}
	}
	
	private Set<IVariable> getVariables(IConstructedTerm t) {
		READ.lock();
		try {
			Set<IVariable> variables = new HashSet<IVariable>();
			for(ITerm term : t.getValue()){
				if(term instanceof IVariable)
					variables.add((IVariable)term);
				if(term instanceof IConstructedTerm)
					variables.addAll(getVariables((IConstructedTerm)term));
			}
			return variables;
		} finally {
			READ.unlock();
		}
	}

	public List<IVariable> getAllVariables() {
		READ.lock();
		try {
			List<IVariable> variables = new ArrayList<IVariable>();
			for(ITerm term : this.terms){
				if(term instanceof IVariable)
					variables.add((IVariable)term);
				if(term instanceof IConstructedTerm)
					variables.addAll(getAllVariables((IConstructedTerm)term));
			}
			return variables;
		} finally {
			READ.unlock();
		}
	}
	
	private List<IVariable> getAllVariables(IConstructedTerm t) {
		READ.lock();
		try {
			List<IVariable> variables = new ArrayList<IVariable>();
			for(ITerm term : t.getValue()){
				if(term instanceof IVariable)
					variables.add((IVariable)term);
				if(term instanceof IConstructedTerm)
					variables.addAll(getAllVariables((IConstructedTerm)term));
			}
			return variables;
		} finally {
			READ.unlock();
		}
	}
}
