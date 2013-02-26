package at.sti2.streamingiris.api.terms;

import java.math.BigDecimal;

/**
 * <p>
 * An interface for representing a numeric term. A numeric term is a constant
 * term which represents a number.
 * </p>
 */
public interface INumericTerm extends IConcreteTerm {

	/**
	 * Returns the value of this numeric term represented as a BigDecimal.
	 * 
	 * @return The BigDecimal representing the value of this numeric term, or
	 *         <code>null</code> if this term represents "NaN", positive
	 *         infinity or negative infinity.
	 */
	public BigDecimal getValue();

	/**
	 * Returns <code>true</code> if this numeric term represents a "NaN" value,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this numeric term represents a "NaN" value,
	 *         <code>false</code> otherwise
	 */
	public boolean isNotANumber();

	/**
	 * Returns <code>true</code> if this numeric term represents positive
	 * infinity, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this numeric term represents positive
	 *         infinity, <code>false</code> otherwise.
	 */
	public boolean isPositiveInfinity();

	/**
	 * Returns <code>true</code> if this numeric term represents negative
	 * infinity, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this numeric term represents negative
	 *         infinity, <code>false</code> otherwise.
	 */
	public boolean isNegativeInfinity();

}
