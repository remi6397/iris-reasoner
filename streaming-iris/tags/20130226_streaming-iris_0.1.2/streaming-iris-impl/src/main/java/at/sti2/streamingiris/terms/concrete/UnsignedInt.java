package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IUnsignedInt;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of UnsignedInt.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedInt extends UnsignedLong implements IUnsignedInt {

	/**
	 * Creates a new UnsignedInt for the specified long.
	 * 
	 * @param value
	 *            The long representing a number not less than 0 and not greater
	 *            than 4294967295.
	 * @throws IllegalArgumentException
	 *             If the specified long is less than 0 or greater than
	 *             4294967295.
	 */
	public UnsignedInt(long value) {
		super(BigInteger.valueOf(value));

		if (value > IUnsignedInt.MAX_INCLUSIVE) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedInt.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_INT.toUri();
	}

}
