package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IUnsignedLong;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of UnsignedLong.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedLong extends NonNegativeInteger implements IUnsignedLong {

	/**
	 * Creates a new UnsignedLong for the specified BigInteger.
	 * 
	 * @param value
	 *            The BigInteger representing a number not less than 0 and not
	 *            greater than 18446744073709551615.
	 * @throws IllegalArgumentException
	 *             If the specified BigInteger is less than 0 or greater than
	 *             18446744073709551615.
	 */
	public UnsignedLong(BigInteger value) {
		super(value);

		if (value.compareTo(IUnsignedLong.MAX_INCLUSIVE) >= 1) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedLong.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_LONG.toUri();
	}

}
