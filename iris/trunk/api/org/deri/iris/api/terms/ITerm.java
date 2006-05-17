package org.deri.iris.api.terms;

import java.util.List;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 12:09:01
 */

/*Type of terms: 

	ConstantTerm, 
	ConstructedTerm,
	NumericTerm,
	StringTerm,
	Variable
	
	More types of terms will be added.
*/
public interface ITerm {
	
	public boolean isGround();
	
	public boolean compare(ITerm term);
	
	//public List<ITerm> getSubTerms();
	
	//public List<ITerm> getTerms();
	
	//public int getType();
	
	public String toString();
	
	public boolean equals(Object o);
	 
	public int hashCode();
}
