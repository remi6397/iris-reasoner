package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.INonPositiveInteger;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of NonPositiveInteger.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonPositiveInteger extends IntegerTerm implements
		INonPositiveInteger {

	/**
	 * Creates a new NonPositiveInteger for the specified integer.
	 * 
	 * @param value
	 *            The integer representing a number not greater than 0.
	 * @throws IllegalArgumentException
	 *             If the specified integer is greater than 0.
	 */
	public NonPositiveInteger(int value) {
		this(BigInteger.valueOf(value));
	}

	/**
	 * Creates a new NonPositiveInteger for the specified BigInteger.
	 * 
	 * @param value
	 *            The BigInteger representing a number not greater than 0.
	 * @throws IllegalArgumentException
	 *             If the specified BigInteger is greater than 0.
	 */
	public NonPositiveInteger(BigInteger value) {
		super(value);

		if (value.compareTo(BigInteger.ZERO) > 0) {
			throw new IllegalArgumentException(
					"Value must not be greater than 0");
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.NON_POSITIVE_INTEGER.toUri();
	}

}
