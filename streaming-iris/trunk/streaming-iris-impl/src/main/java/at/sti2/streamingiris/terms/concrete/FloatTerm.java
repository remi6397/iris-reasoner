package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;
import java.net.URI;

import at.sti2.streamingiris.api.terms.concrete.IFloatTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;

/**
 * <p>
 * Simple implementation of the IFloatTerm.
 * </p>
 */
public class FloatTerm extends AbstractNumericTerm implements IFloatTerm {

	private final Float value;

	private BigDecimal decimalValue;

	/**
	 * Constructs a new float with the given value.
	 * 
	 * @param value
	 *            the float value for this object
	 * @throws NullPointerException
	 *             if the float is null
	 */
	FloatTerm(float value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		if (isNotANumber() || isPositiveInfinity() || isNegativeInfinity()) {
			return null;
		}

		if (decimalValue == null) {
			decimalValue = new BigDecimal(Float.toString(value));
		}

		return decimalValue;
	}

	public boolean isNotANumber() {
		return value.equals(Float.NaN);
	}

	public boolean isPositiveInfinity() {
		return value.equals(Float.POSITIVE_INFINITY);
	}

	public boolean isNegativeInfinity() {
		return value.equals(Float.NEGATIVE_INFINITY);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	public URI getDatatypeIRI() {
		return XmlSchemaDatatype.FLOAT.toUri();
	}

}
