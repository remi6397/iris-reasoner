package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IPositiveInteger;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of PositiveInteger.
 * </p>
 * 
 * @author Adrian Marte
 */
public class PositiveInteger extends IntegerTerm implements IPositiveInteger {

	/**
	 * Creates a new PositiveInteger for the specified integer.
	 * 
	 * @param value
	 *            The integer representing a number not less than 1.
	 * @throws IllegalArgumentException
	 *             If the specified integer is less than 1.
	 */
	public PositiveInteger(int value) {
		this(BigInteger.valueOf(value));
	}

	/**
	 * Creates a new PositiveInteger for the specified BigInteger.
	 * 
	 * @param value
	 *            The BigInteger representing a number not less than 1.
	 * @throws IllegalArgumentException
	 *             If the specified BigInteger is less than 1.
	 */
	public PositiveInteger(BigInteger value) {
		super(value);

		if (value.compareTo(BigInteger.ONE) < 0) {
			throw new IllegalArgumentException("Value must not be less than 1");
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.POSITIVE_INTEGER.toUri();
	}

}
