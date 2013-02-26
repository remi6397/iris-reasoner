package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IUnsignedShort;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of UnsignedShort.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedShort extends UnsignedInt implements IUnsignedShort {

	/**
	 * Creates a new UnsignedShort for the specified integer.
	 * 
	 * @param value
	 *            The integer representing a number not less than 0 and not
	 *            greater than 65535.
	 * @throws IllegalArgumentException
	 *             If the specified integer is less than 0 or greater than
	 *             65535.
	 */
	public UnsignedShort(int value) {
		super(value);

		if (value > IUnsignedShort.MAX_INCLUSIVE) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedShort.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_SHORT.toUri();
	}

}
