/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.deri.iris.api.basics;

import java.util.List;

import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 11:59:34
 */
public interface IAtom {
	
	// no need for this, ontobroker too
	//public void setPredicate(IPredicate p);
	
	public IPredicate getPredicate();
	
	public void setTerms(List<ITerm> terms);
	
	public List<ITerm> getTerms();
	
	public void setTerm(ITerm term, int arg);
	
	public ITerm getTerm(int arg);
	
	/**
	 *  If isGround() is false for an atom, using
	 *  getTerms() and getTypeOfTerm() we can get variables
	 *  of the atom. Do we need this?
	 */
	//public IVariable[] getVariables();
	//public void setVariables(IVariable[] variables);
	
	public boolean isGround();
	
	public boolean isInCycle();
	
	// is the atom "sure", "true" or...
	public boolean isKnown();
	
	// public boolean equals(Object o);
	
	public boolean compare(IAtom atom);
	
	//public String toString();
	
	//public int hashCode();
}
