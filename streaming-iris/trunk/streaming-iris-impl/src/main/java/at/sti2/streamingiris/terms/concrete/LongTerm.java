package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.ILongTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * A simple implementation of Long.
 * </p>
 * 
 * @author Adrian Marte
 */
public class LongTerm extends IntegerTerm implements ILongTerm {

	/**
	 * Creates a new LongTerm for the specified Long.
	 * 
	 * @param value
	 *            The Long value.
	 */
	public LongTerm(long value) {
		super(BigInteger.valueOf(value));
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.LONG.toUri();
	}

}
