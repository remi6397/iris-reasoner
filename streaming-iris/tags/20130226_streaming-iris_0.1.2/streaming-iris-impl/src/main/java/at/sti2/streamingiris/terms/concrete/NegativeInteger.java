package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.INegativeInteger;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NonPositiveInteger.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NegativeInteger extends IntegerTerm implements INegativeInteger {

	/**
	 * Creates a new NegativeInteger for the specified integer.
	 * 
	 * @param value
	 *            The integer representing a number not greater than -1.
	 * @throws IllegalArgumentException
	 *             If the specified integer is greater than -1.
	 */
	public NegativeInteger(int value) {
		this(BigInteger.valueOf(value));
	}

	/**
	 * Creates a new NegativeInteger for the specified BigInteger.
	 * 
	 * @param value
	 *            The BigInteger representing a number not greater than -1.
	 * @throws IllegalArgumentException
	 *             If the specified BigInteger is greater than -1.
	 */
	public NegativeInteger(BigInteger value) {
		super(value);

		if (value.compareTo(BigInteger.ONE.negate()) > 0) {
			throw new IllegalArgumentException(
					"Value must not be greater than -1");
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NEGATIVE_INTEGER.toUri();
	}

}
