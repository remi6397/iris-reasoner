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
package org.deri.iris.api.terms;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * An interface for representing a constructed term (function symbol). 
 * A constructed term is a term built from function-s and subter-s.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 11:34:59
 */
public interface IConstructedTerm extends ITerm{

	public List<ITerm> getValue();
	
	/**
	 * Set the name of the constructed term (function symbol).
	 * 
	 * @param arg	The name of the constructed term.
	 */
	public void setFunctionSymbol(String arg);
	
	/**
	 * Get the name of the constructed term (function symbol).
	 * 
	 * @return	The name of the constructed term.
	 */
	public String getFunctionSymbol();
	
	/**
	 * Returns the arity of the constructed term (function symbol).
	 * 
	 * @return The arity of the constructed term.
	 */
	public int getArity();
	
	/**
	 * Returns the parameter at the specified position in this constructed term.
	 * 
	 * @param arg	Index of parameter to return.
	 * @return		The parameter at the specified position in this list.
	 * @throws 		java.lang.IndexOutOfBoundsException - 
	 * 				if the index is out of range (index < 0 || index >= size()).
	 */
	public ITerm getParameter(int arg) throws java.lang.IndexOutOfBoundsException;

	/**
	 * Returns a list of all terms from this constructed term (function symbol).
	 * 
	 * @return	List of all terms from this constructed term.
	 */
	public List<ITerm> getParameters();
	
	/**
	 * Returns all distinct variables from this tupple.
	 * 
	 * @return All distinct variables from this tupple.
	 */
	public Set<IVariable> getVariables();
}
