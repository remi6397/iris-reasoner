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
public interface IConstructedTerm
						extends ITerm<IConstructedTerm, List<ITerm>>{

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
	 * @return	ist of all terms from this constructed term.
	 */
	public List<ITerm> getParameters();
	
	/**
	 * Returns all distinct variables from this tupple.
	 * 
	 * @return All distinct variables from this tupple.
	 */
	public Set<IVariable> getVariables();
}
