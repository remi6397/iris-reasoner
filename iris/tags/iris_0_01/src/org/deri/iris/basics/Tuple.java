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

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 12:07:31
 */
public class Tuple implements ITuple{

	private List<ITerm> terms = null;
	
	/// Correct it!
	private ITuple duplicate = null;
	
	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();
	
	
	Tuple(List<ITerm> terms){
		this.terms = terms;
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
		if(this.terms.set(index, term) != null){
			return true;
		}
		return false;
	}

	public boolean setTerms(int index, List<ITerm> terms) {
		WRITE.lock();
		try {
			return this.terms.addAll(index, terms);
		} finally {
			WRITE.unlock();
		}
	}

	public String toString(){
		String s = "<";
		for(int i=0; i<this.getArity(); i++){
			s = s + this.getTerm(i).toString();
			if(i<this.getArity()-1)s = s + ", ";
			else s = s + ">";
		}
		return s;
	}

	/// Correct it!
	public void setDuplicate(ITuple duplicate) {
		this.duplicate = duplicate;
	}
	/// Correct it!
	public ITuple getDuplicate() {
		return this.duplicate;
	}
}
