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

package org.deri.iris.api.basics;

import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
* @author Darko Anicic, DERI Innsbruck
* @date   07.12.2005 08:45:24
*/

public interface ITuple extends Comparable<ITuple>{

	public int getArity();
	
	public ITerm getTerm(int arg);
	
	public List<ITerm> getTerms();
	
	public boolean setTerm(int index, ITerm term);
	
	/**
	 * Inserts all of the elements in the specified collection into this 
	 * list (starting from 0th position). The new elements 
	 * will appear in this list in the order that they are returned by 
	 * the specified collection's iterator. This method is thread-save.
	 * 
	 * @param terms
	 * 				- elements to be inserted into this list.
	 * @return true 
	 * 				if this list changed as a result of the call.
	 */
	public boolean setTerms(List<ITerm> terms);
	
	/**
	 * Inserts all of the elements in the specified collection into this 
	 * list at the specified position. Shifts 
	 * the element currently at that position (if any) and any subsequent 
	 * elements to the right.
	 * Replaces the element currently at that position (if any) and any 
	 * subsequent elements to the right. It does not increases their indices. 
	 * Thus an IndexOutOfBoundsException exception will be thrown if size of
	 * a term list exceeds the number of available positions, which is: 
	 * 'tuple arity' - index.  
	 * The new elements will appear in this list in the order that they are 
	 * returned by the specified collection's iterator. This method is 
	 * thread-save.
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
	
	public boolean isGround();
	
	public Set<IVariable> getVariables();
	
	/**
	 * Tuples t0 and t1 are duplicates if they have identical terms  
	 * for each sort index. Used in operations on relations 
	 * (e.g. for tuple sorting � IndexComparator) to increase the 
	 * efficiency of the operations.
	 * 
	 * @param duplicate
	 * 					a tuple that will be stored in a list of 
	 * 					duplicates for the entire tuple.  
	 * @deprecated a duplicate shouldn't be stored in the tuple
	 */
	public void setDuplicate(ITuple duplicate);
	
	/**
	 * @deprecated a duplicate shouldn't be stored in the tuple
	 */
	public ITuple getDuplicate();
}
