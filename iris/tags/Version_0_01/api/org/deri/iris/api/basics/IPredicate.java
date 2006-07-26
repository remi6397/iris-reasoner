package org.deri.iris.api.basics;



/**
* @author Darko Anicic, DERI Innsbruck
* @date   07.01.2006 12:00:00
*/

public interface IPredicate extends Comparable<IPredicate> {

    public void setPredicateSymbol(String name);
    
	public String getPredicateSymbol();
	
	public int getArity();
	
	public void setBuiltIn(boolean arg);
	
	public boolean isBuiltIn();
}
