package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to XMLLiteral instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * </ul>
 */
public class ToXMLLiteralBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_XMLLITERAL", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToXMLLiteralBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IXMLLiteral) {
			return term;
		} else if (term instanceof IStringTerm) {
			return toXMLLiteral((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a XMLLiteral term.
	 * 
	 * @param term
	 *            The String term to be converted.
	 * @return A new XMLLiteral term representing the result of the conversion.
	 */
	public static IXMLLiteral toXMLLiteral(IStringTerm term) {
		String value = term.getValue();
		return CONCRETE.createXMLLiteral(value);
	}

}
