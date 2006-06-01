package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.INumericTerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 20.12.2005 17:02:19
 */
public interface IDecimalTerm extends INumericTerm<IDecimalTerm> {

	public void setValue(double arg);

	public double getValue();

}
