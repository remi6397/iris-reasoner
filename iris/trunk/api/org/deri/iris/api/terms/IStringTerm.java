package org.deri.iris.api.terms;

/**
 * <p>
 * An interface for representing a string term. A string term is 
 * a constant term which represents a string.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   20.12.2005 16:33:02
 */
public interface IStringTerm<Type extends IStringTerm> 
									extends ITerm <Type, String>{
}
