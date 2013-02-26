package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IPlainLiteral;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to PlainLiteral instances. The following data types are
 * supported:
 * <ul>
 * <li>String</li>
 * <li>All data types for which casting to string is supported</li>
 * </ul>
 */
public class ToPlainLiteralBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_TEXT", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToPlainLiteralBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		return toPlainLiteral(term);
	}

	/**
	 * Converts a XMLLiteral term to a PlainLiteral term.
	 * 
	 * @param term
	 *            The XMLLiteral term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion.
	 */
	public static IPlainLiteral toPlainLiteral(IXMLLiteral term) {
		return CONCRETE.createPlainLiteral(term.getString(), term.getLang());
	}

	/**
	 * Converts a String term to a PlainLiteral term.
	 * 
	 * @param term
	 *            The String term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion.
	 */
	public static IPlainLiteral toPlainLiteral(IStringTerm term) {
		String value = term.getValue();
		return CONCRETE.createPlainLiteral(value);
	}

	/**
	 * Converts a constant term to a PlainLiteral term. For data types other
	 * than Text and XMLLiteral, the <code>toCanonicalString</code> method of
	 * the given term is used to convert to first convert to a String term and
	 * then to a PlainLiteral term.
	 * 
	 * @param term
	 *            The term to be converted.
	 * @return A new PlainLiteral term representing the result of the
	 *         conversion, or <code>null</code> if the data type represented by
	 *         the given term is not supported.
	 */
	public static IPlainLiteral toPlainLiteral(ITerm term) {
		if (term instanceof IConcreteTerm) {
			if (term instanceof IPlainLiteral) {
				return (IPlainLiteral) term;
			} else if (term instanceof IStringTerm) {
				return toPlainLiteral((IStringTerm) term);
			} else if (term instanceof IXMLLiteral) {
				return toPlainLiteral((IXMLLiteral) term);
			}

			IStringTerm string = ToStringBuiltin.toString(term);

			if (string != null) {
				return toPlainLiteral(string);
			}
		}

		return null;
	}

}
