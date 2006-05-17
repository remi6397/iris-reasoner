package org.deri.iris.api.terms;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   20.12.2005 16:33:02
 */
public interface IStringTerm extends ITerm{

	public void setValue(String arg);
	
	public String getValue();
}
