package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.INumericTerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   06.01.2006 17:31:11
 */
public interface IFloatTerm extends INumericTerm{

	public void setValue(float arg);
	
	public float getValue();
	
}
