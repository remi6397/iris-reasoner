package org.deri.iris.terms;

import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 12:29:03
 */
public class Term implements ITerm{

	//private Object term;
	private String term;
	private boolean ground = false;
	
	@SuppressWarnings("unchecked")
	//public Term(ITerm term){
	public Term(String term){
		this.term = term;
		//this.ground = term.isGround();
	}
	
	public boolean isGround() {
		return this.ground;
	}

	// Correct it! (currently string comparison)
	public int compareTo(Object o) {
		Term term = (Term)o;
		int i = this.term.compareTo(term.term);
		
		return i;
	}
	
	public String toString(){
		return this.term;
	}

	/* Override this method in specific implementation of each term.
	 * (non-Javadoc)
	 * @see org.deri.iris.api.terms.ITerm#getMinValue()
	 */
	public ITerm getMinValue() {
		return null;
	}

}
