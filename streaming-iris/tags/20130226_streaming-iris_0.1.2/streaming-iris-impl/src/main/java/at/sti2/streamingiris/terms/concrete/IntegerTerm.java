package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IIntegerTerm.
 * </p>
 */
public class IntegerTerm extends DecimalTerm implements IIntegerTerm {

	IntegerTerm(int value) {
		this(BigInteger.valueOf(value));
	}

	IntegerTerm(BigInteger value) {
		super(new BigDecimal(value));
	}

	@Override
	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.INTEGER.toUri();
	}

}
