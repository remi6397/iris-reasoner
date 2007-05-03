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
 * <p>
 * Represents a tuple. A tuple is a list of terms which represents a record in a
 * relation.
 * </p>
 * <p>
 * $Id: ITuple.java,v 1.10 2007-05-03 12:01:07 darko_anicic Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 07.12.2005 08:45:24
 */

public interface ITuple extends Comparable<ITuple> {

	/**
	 * Returns the arity of the predicate.
	 * 
	 * @return The arity of the predicate.
	 */
	public int getArity();

	/**
	 * Returns the term at the specified position in this tuple.
	 * 
	 * @param index
	 *            Index of tuple to return.
	 * @return The term at the specified position in this tuple.
	 */
	public ITerm getTerm(int index);

	/**
	 * Returns all terms of this tuple preserving the order of terms in the
	 * tuple.
	 * 
	 * @return Returns all terms of this tuple.
	 */
	public List<ITerm> getTerms();

	/**
	 * Replaces the term at the specified position in this tuple with the
	 * specified term.
	 * 
	 * @param index
	 *            Index of element to replace.
	 * @param term
	 *            Term to be stored at the specified position.
	 * @return The term which was previously placed at the specified position.
	 */
	public ITerm setTerm(int index, ITerm term);

	/**
	 * Inserts all of the elements in the specified collection into this list
	 * (starting from 0th position). The new elements will appear in this list
	 * in the order that they are returned by the specified collection's
	 * iterator. This method is thread-save.
	 * 
	 * @param terms
	 *            Elements to be inserted into this list.
	 * @return <code>true</code> if this list has been changed as a result of
	 *         the call; <code>false</code> otherwise.
	 */
	public boolean setTerms(List<ITerm> terms);

	/**
	 * Inserts all of the elements in the specified collection into this list at
	 * the specified position. Shifts the element currently at that position (if
	 * any) and any subsequent elements to the right. Replaces the element
	 * currently at that position (if any) and any subsequent elements to the
	 * right. It does not increases their indices. Thus an
	 * IndexOutOfBoundsException exception will be thrown if size of a term list
	 * exceeds the number of available positions, which is: 'tuple arity' -
	 * index. The new elements will appear in this list in the order that they
	 * are returned by the specified collection's iterator. This method is
	 * thread-save.
	 * 
	 * @param index
	 *            Index at which to insert first element from the specified
	 *            collection.
	 * @param terms
	 *            Elements to be inserted into this list.
	 * @return <code>true</code> if this list has been changed as a result of
	 *         the call; <code>false</code> otherwise.
	 */
	public boolean setTerms(int index, List<ITerm> terms);

	/**
	 * Checks whether this tuple contains only ground terms.
	 * 
	 * @return <code>true</code> if all terms of this tuple are grounded;
	 *         <code>false</code> otherwise.
	 */
	public boolean isGround();

	/**
	 * Returns all distinct variables from this tupple.
	 * 
	 * @return All distinct variables from this tupple.
	 */
	public Set<IVariable> getVariables();
	
	/**
	 * Returns all variables from this tupple.
	 * 
	 * @return All variables from this tupple.
	 */
	public List<IVariable> getAllVariables();

	/**
	 * Tuples t0 and t1 are duplicates if they have identical terms for each
	 * index. Used with implementations of relational operations (e.g. for
	 * sorting tuples with IndexComparator) to increase the efficiency of the
	 * operations.
	 * 
	 * @param duplicate
	 *            A tuple that will be stored in a list of duplicates for the
	 *            entire tuple.
	 * @deprecated a duplicate shouldn't be stored in the tuple
	 */
	public void setDuplicate(ITuple duplicate);

	/**
	 * @deprecated a duplicate shouldn't be stored in the tuple
	 */
	public ITuple getDuplicate();
}
