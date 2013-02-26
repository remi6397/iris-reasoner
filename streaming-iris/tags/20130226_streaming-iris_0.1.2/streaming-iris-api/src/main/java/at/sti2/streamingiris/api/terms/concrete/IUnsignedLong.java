package at.sti2.streamingiris.api.terms.concrete;

import java.math.BigInteger;

/**
 * <p>
 * Represents the XML Schema datatype xsd:unsignedLong.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface IUnsignedLong extends INonNegativeInteger {

	/**
	 * The maximal value of unsigned long.
	 */
	public static final BigInteger MAX_INCLUSIVE = new BigInteger(
			"18446744073709551615");

}
