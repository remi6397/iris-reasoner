package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IDecimalTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of IDecimalTerm.
 * </p>
 */
public class DecimalTerm extends AbstractNumericTerm implements IDecimalTerm {

	protected final BigDecimal value;

	DecimalTerm(double value) {
		this(new BigDecimal(Double.toString(value)));
	}

	DecimalTerm(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.DECIMAL.toUri();
	}

	public boolean isNotANumber() {
		return false;
	}

	public boolean isPositiveInfinity() {
		return false;
	}

	public boolean isNegativeInfinity() {
		return false;
	}

}
