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
	
	public boolean equals(Object o);
	
	public String toString();
	
	//public boolean isGround();
	
	//public void print(PrintStream p);
	
	//public int hashCode();
}
