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
 * type instances to HexBinary instances. The following data types are
 * supported:
 * <ul>
 * <li>Base64</li>
 * <li>String</li>
 * </ul>
 */
public class ToHexBinaryBuiltin extends ConversionBuiltin {

	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"TO_HEXBINARY", 2);

	/**
	 * Creates a new instance of this builtin.
	 * 
	 * @param terms
	 *            The term representing the data type instance to be converted.
	 */
	public ToHexBinaryBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm convert(ITerm term) {
		if (term instanceof IHexBinary) {
			return term;
		} else if (term instanceof IBase64Binary) {
			return toHexBinary((IBase64Binary) term);
		} else if (term instanceof IStringTerm) {
			return toHexBinary((IStringTerm) term);
		}

		return null;
	}

	/**
	 * Converts a String term to a HexBinary term.
	 * 
	 * @param term
	 *            The String term to be converted.
	 * @return A new HexBinary term representing the result of the conversion,
	 *         or <code>null</code> if the conversion fails.
	 */
	public static IHexBinary toHexBinary(IStringTerm term) {
		String binary = term.getValue();
		return toHexBinary(binary);
	}

	/**
	 * Converts a Base64 term to a HexBinary term.
	 * 
	 * @param term
	 *            The Base64 term to be converted.
	 * @return A new HexBinary term representing the result of the conversion.
	 */
	public static IHexBinary toHexBinary(IBase64Binary term) {
		String binary = term.getValue();
		return toHexBinary(binary);
	}

	private static IHexBinary toHexBinary(String binary) {
		try {
			return CONCRETE.createHexBinary(binary);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"The specified string does not represent a hex binary value",
					e);
		}
	}

}
