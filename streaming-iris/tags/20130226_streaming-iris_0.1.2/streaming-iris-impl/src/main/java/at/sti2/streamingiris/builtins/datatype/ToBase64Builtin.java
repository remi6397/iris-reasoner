package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IBase64Binary;
import at.sti2.streamingiris.api.terms.concrete.IHexBinary;

/**
 * Represents a data type conversion function, which converts supported data
 * type instances to Base64 instances. The following data types are supported:
 * <ul>
 * <li>HexBinary</li>
 * <li>String</li>
 * </ul>
 */
public class ToBase64Builtin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_BASE64", 2);

	/**
	 * Creates a new instance of this built-in.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToBase64Builtin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IHexBinary) {
			return toBase64((IHexBinary) term);
		} else if (term instanceof IBase64Binary) {
			return term;
		} else if (term instanceof IStringTerm) {
			return toBase64((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a Base64 term.
	 * 
	 * @param term
	 *            The String term to be converted.
	 * @return A new Base64 term representing the result of the conversion.
	 */
	public static IBase64Binary toBase64(IStringTerm term) {
		String binary = term.getValue();
		return toBase64(binary);
	}

	/**
	 * Converts a HexBinary term to a Base64 term.
	 * 
	 * @param term
	 *            The HexBinary term to be converted.
	 * @return A new Base64 term representing the result of the conversion.
	 */
	public static IBase64Binary toBase64(IHexBinary term) {
		String binary = term.getValue();
		return toBase64(binary);
	}

	private static IBase64Binary toBase64(String binary) {
		return CONCRETE.createBase64Binary(binary);
	}

}
