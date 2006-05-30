package org.deri.iris.api.basics;

import java.util.List;

import org.deri.iris.api.terms.ITerm;

/**
* @author Darko Anicic, DERI Innsbruck
* @date   07.12.2005 08:45:24
*/

public interface ITuple {

	public int getArity();
	
	public ITerm getTerm(int arg);
	
	public List<ITerm> getTerms();
	
	public boolean setTerm(int index, ITerm term);
	
	/**
	 * Inserts all of the elements in the specified collection into this 
	 * list at the specified position. Shifts 
	 * the element currently at that position (if any) and any subsequent 
	 * elements to the right (increases their indices). The new elements 
	 * will appear in this list in the order that they are returned by 
	 * the specified collection's iterator. This method is thread-save.
	 * 
	 * @param index
	 * 				- index at which to insert first element from 
	 * 				the specified collection.
	 * @param terms
	 * 				- elements to be inserted into this list.
	 * @return true 
	 * 				if this list changed as a result of the call.
	 */
	public boolean setTerms(int index, List<ITerm> terms);
	
	public boolean equals(Object o);
	
	public String toString();
	
	//public boolean isGround();
	
	//public void print(PrintStream p);
	
	//public int hashCode();
}
