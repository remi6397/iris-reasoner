package org.deri.iris.api.basics;

import java.util.List;

import org.deri.iris.api.terms.IVariable;

/**
 * Interface or class description
 *
 *
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 17:21:21
 */

public interface IHead {

	public int getHeadLenght();
	
	public ILiteral getHeadLiteral(int arg);
	
	public List<ILiteral> getHeadLiterals();
	
	public List<IVariable> getHeadVariables();
}
