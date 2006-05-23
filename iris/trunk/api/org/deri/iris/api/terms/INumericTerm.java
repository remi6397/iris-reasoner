package org.deri.iris.api.terms;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   20.12.2005 16:32:16
 */
public interface INumericTerm<Type extends INumericTerm> extends ITerm<Type>{
	
	public enum TypeOfNumericTerm {
		IDecimalTerm,
		IIntegerTerm,
		IFloatTerm,
		IDoubleTerm
		// more types of numeric terms might be added here
    };
    
	public INumericTerm.TypeOfNumericTerm getTypeOfNumericTerm();
	
}
