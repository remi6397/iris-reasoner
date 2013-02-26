package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IDoubleTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IDoubleTerm.
 * </p>
 */
public class DoubleTerm extends AbstractNumericTerm implements IDoubleTerm {

	private final Double value;

	private BigDecimal decimalValue;

	DoubleTerm(float value) {
		this(Double.valueOf(Float.toString(value)));
	}

	DoubleTerm(double value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		if (isNotANumber() || isPositiveInfinity() || isNegativeInfinity()) {
			return null;
		}

		if (decimalValue == null) {
			decimalValue = new BigDecimal(Double.toString(value));
		}

		return decimalValue;
	}

	public boolean isNotANumber() {
		return value.equals(Double.NaN);
	}

	public boolean isPositiveInfinity() {
		return value.equals(Double.POSITIVE_INFINITY);
	}

	public boolean isNegativeInfinity() {
		return value.equals(Double.NEGATIVE_INFINITY);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.DOUBLE.toUri();
	}

}
