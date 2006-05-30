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

	private List<ITerm> terms;

	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();
	
	
	public Tuple(List<ITerm> terms){
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
}
