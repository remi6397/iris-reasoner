package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IUnsignedByte;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of UnsignedByte.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedByte extends UnsignedShort implements IUnsignedByte {

	/**
	 * Creates a new UnsignedByte for the specified short.
	 * 
	 * @param value
	 *            The short representing a number not less than 0 and not
	 *            greater than 255.
	 * @throws IllegalArgumentException
	 *             If the specified short is less than 0 or greater than 255.
	 */
	public UnsignedByte(short value) {
		super(value);

		if (value > IUnsignedByte.MAX_INCLUSIVE) {
			throw new IllegalArgumentException(
					"Value must not be greater than "
							+ IUnsignedByte.MAX_INCLUSIVE);
		}
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_BYTE.toUri();
	}

}
