package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.INonNegativeInteger;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NonNegativeInteger.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonNegativeInteger extends IntegerTerm implements
		INonNegativeInteger {

	/**
	 * Creates a new NonNegativeInteger for the specified integer.
	 * 
	 * @param value
	 *            The integer representing a number not less than 0.
	 * @throws IllegalArgumentException
	 *             If the specified integer is less than 0.
	 */
	public NonNegativeInteger(int value) {
		this(BigInteger.valueOf(value));
	}

	/**
	 * Creates a new NonNegativeInteger for the specified BigInteger.
	 * 
	 * @param value
	 *            The BigInteger representing a number not less than 0.
	 * @throws IllegalArgumentException
	 *             If the specified BigInteger is less than 0.
	 */
	public NonNegativeInteger(BigInteger value) {
		super(value);

		if (value.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException("Value must not be less than 0");
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NON_NEGATIVE_INTEGER.toUri();
	}

}
