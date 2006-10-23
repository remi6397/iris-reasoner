package org.deri.iris.api.terms;

import java.util.List;
import java.util.Set;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 11:34:59
 */
public interface IConstructedTerm
						extends ITerm<IConstructedTerm, List<ITerm>>{

	public void setFunctionSymbol(String arg);
	
	public String getFunctionSymbol();
	
	public int getArity();
	
	public ITerm getParameter(int arg) throws java.lang.IndexOutOfBoundsException;

	public List<ITerm> getParameters();
	
	public Set<IVariable> getVariables();
}
